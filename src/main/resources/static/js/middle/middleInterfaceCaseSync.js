// 同步数据
function syncData() {
    var projectId = $('#ProjectId').val();
    var fromEnvId = $('#FromEnv').val();
    var toEnvId = $('#ToEnv').val();

    if (fromEnvId == toEnvId) {
        alert("请选择不同环境")
        return;
    }


    url = "/middle/syncMiddleCase?projectId="+ projectId +"&fromEnvId=" + fromEnvId + "&toEnvId=" + toEnvId;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        async: true,
        timeout: 6000,
        success: function (data) {
            alert("正在同步，请检查日志查看同步结果")
        },
        error: function (e) {
            alert("run fail:" + JSON.stringify(e, null, 4));
        }

    });


}

