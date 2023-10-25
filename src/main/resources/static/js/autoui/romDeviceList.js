//获取设备列表
function getDeviceList() {
    $("#deviceResult").text("");
    $("#deviceResult").text("运行中...");
    var url = "http://okay-qaclient.xk12.cn/api/getdevice";
    $.ajax({
        type: "POST",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 6000,
        success: function (data) {

            if (data.code == 200) {

                var deviceInfo = data.data;
                $("#deviceResult").text("完成");
                $("#deviceDesc").text("设备总数:" + deviceInfo.length);
            } else {
                alert("获取失败：" + JSON.stringify(data));
                $("#deviceResult").text("");
            }

        },
        error: function (e) {
            alert("获取失败：" + JSON.stringify(e));
            $("#deviceResult").text("");
        }
    });


};


function installTestApp() {

    $("#isInstallTestApp").text("");
    $("#isInstallTestApp").text("运行中...");
    var apkName = $("#installTestAppDesc").val().trim();
    ;
    var url = "http://okay-qaclient.xk12.cn/api/installapk?apkName=" + apkName;

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 600000,
        success: function (data) {

            if (data.code == 200) {

                $("#isInstallTestApp").text("完成");

            } else {
                alert("获取失败：" + data.msg);
                $("#isInstallTestApp").text("");
            }
        },
        error: function (e) {
            alert("获取失败：" + JSON.stringify(e));
            $("#isInstallTestApp").text("");
        }
    });
};

function installApp() {

    $("#isInstallApp").text(" ");
    $("#isInstallApp").text("运行中...");
    var apkName = $("#installAppDesc").val().trim();
    ;
    var url = "http://okay-qaclient.xk12.cn/api/installapk?apkName=" + apkName;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 600000,
        success: function (data) {

            if (data.code == 200) {

                $("#isInstallApp").text("完成");
            } else {
                alert("获取失败：" + data.msg);
                $("#isInstallApp").text(" ");
            }
        },
        error: function (e) {
            alert("获取失败：" + JSON.stringify(e));
            $("#isInstallApp").text(" ");
        }
    });
};


<!--push账号-->
function pushAccount() {

    var fileName = $("#pushAccountDesc").val().trim();
    ;
    $("#isPushAccount").text(" ");
    $("#isPushAccount").text("运行中...");
    var url = "http://okay-qaclient.xk12.cn/api/pushstudentaccount?fileName=" + fileName;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 600000,
        success: function (data) {

            if (data.code == 200) {

                $("#isPushAccount").text("完成");
            } else {
                alert("获取失败：" + data.msg);
                $("#isPushAccount").text(" ");
            }
        },
        error: function (e) {
            alert("获取失败：" + JSON.stringify(e));
            $("#isPushAccount").text(" ");
        }
    });
};


var addObj;

//添加学生账号模态框
function showAddModal(obj) {
    addObj = obj;
    $("#addModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//添加账号
function addModelID() {


    var NAME = $('#addStudentName').val().trim();
    var COMMENTS = $('#addStudentComments').val().trim().split("\n").join(";");

    if (!NAME) {
        alert("请输入文件名");
        return false;
    }
    if (!COMMENTS) {
        alert("请输入学生们");
        return false;
    }

    var url = "http://okay-qaclient.xk12.cn/api/addstudentaccount?fineName=" + NAME + "&studentList=" + COMMENTS;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，刷新页面
                $("#addModal").modal('hide');
                location.reload();

            } else {
                alert("本地文件添加失败：" + JSON.stringify(data));
            }

        },
        error: function (e) {
            alert("本地文件添加失败：" + JSON.stringify(e));
        }
    });

};


function runAdb() {

    $("#isRunAdb").text(" ");
    $("#isRunAdb").text("运行中...");
    var cmd = $("#runAdbDesc").val();
    var url = "http://okay-qaclient.xk12.cn/api/runshell?cmd=" + cmd;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 600000,
        success: function (data) {

            if (data.code == 200) {

                $("#isRunAdb").text("完成");
            } else {
                alert("获取失败：" + data.msg);
                $("#isRunAdb").text(" ");
            }
        },
        error: function (e) {
            alert("获取失败：" + JSON.stringify(e));
            $("#isRunAdb").text(" ");
        }
    });
};
