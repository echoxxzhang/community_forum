package com.echoxxzhang.service;

import com.echoxxzhang.dao.LoginTicketMapper;
import com.echoxxzhang.dao.UserMapper;
import com.echoxxzhang.entity.LoginTicket;
import com.echoxxzhang.entity.User;
import com.echoxxzhang.util.CommunityUtil;
import com.echoxxzhang.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.echoxxzhang.util.CommunityConstant.*;

@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        // todo 1 多if优化
        // 判断user的各种信息不为空
        if (user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwdMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("EmailMsg","邮箱不能为空");
            return map;
        }

        // 验证账号
        User name = userMapper.selectByName(user.getUsername());
        if (name != null){
            map.put("usernameMsg","该账号已存在");
        }

        // 验证邮箱
        User e = userMapper.selectByEmail(user.getEmail());
        if (e != null){
            map.put("EmailMsg","该邮箱已被注册!");
        }

        // 验证通过，注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));

        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail()); // 设置变量给thymeleaf
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context); // 创建邮箱模板
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    /**
     * 判断用户是否已激活
     * @param userId
     * @param code
     * @return
     */
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 用户登陆
     * @param username
     * @param password
     * @param expiredSeconds 过期时间（s）
     * @return
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // todo 2 多if优化
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());  // 返回给客户端一个ticker凭证
        return map;
    }

    /**
     *  用户登出
     * @param ticket
     */
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }


    /**
     * 返回ticket
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    /**
     * 更新用户头像
     * @param userId
     * @param headUrl
     * @return
     */
    public int updateHeader(int userId, String headUrl){
        return userMapper.updateHeader(userId, headUrl);
    }

    /**
     * 更新用户密码
     * @param userId
     * @param passwd
     * @return
     */
    public int updatePasswd(int userId, String passwd){
        return userMapper.updatePassword(userId, passwd);
    }

}
