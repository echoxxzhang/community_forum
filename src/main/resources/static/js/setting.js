$(function(){
    $("#uploadForm").submit(upload);
});

function upload() {
    $.ajax({
        url: "http://upload-z2.qiniup.com",
        method: "post",
        processData: false,  // 不要把表单的数据转为字符串
        contentType: false, // 不让jquery设置上传类型
        data: new FormData($("#uploadForm")[0]),
        success: function(data) {
            if(data && data.code == 0) {

                $.post(  // 更新头像访问路径
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function(data) {
                        data = $.parseJSON(data); // 解析成json
                        if(data.code == 0) {
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!");
            }
        }
    });
    return false;  // 表示事件到此为止
}