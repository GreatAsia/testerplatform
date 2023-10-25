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

//发送dev环境数据
function sendDevInfo() {

    var env = $('#dev_env').text().trim();
    var username = $('#dev_username').val().trim();
    var password = $('#dev_password').text().trim();
    var url_header = $('#dev_host').text().trim();
    var exerciseid = $('#dev_exercise').val().trim();
    var courseid = $('#dev_coursePackage').val().trim();


    var SAVEDATA = [];
    var requestData = {
        "username": username,
        "password": password,
        "url_header": url_header,
        "exerciseid": exerciseid,
        "courseid": courseid,
        "env": env
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/publish",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {
            alert("send  " + JSON.stringify(data));
        },
        error: function (e) {
            alert("send fail" + JSON.stringify(e));
        }

    });


};


//发送hotfix环境数据
function sendHotfixInfo() {

    var env = $('#hotfix_env').text().trim();
    var username = $('#hotfix_username').val().trim();
    var password = $('#hotfix_password').text().trim();
    var url_header = $('#hotfix_host').text().trim();
    var exerciseid = $('#hotfix_exercise').val().trim();
    var courseid = $('#hotfix_coursePackage').val().trim();

    var SAVEDATA = [];
    var requestData = {
        "username": username,
        "password": password,
        "url_header": url_header,
        "exerciseid": exerciseid,
        "courseid": courseid,
        "env": env
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/publish",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {
            alert("send  " + JSON.stringify(data));
        },
        error: function (e) {
            alert("send fail" + JSON.stringify(e));
        }

    });


};


//发送online环境数据
function sendOnlineInfo() {

    var env = $('#online_env').text().trim();
    var username = $('#online_username').val().trim();
    var password = $('#online_password').text().trim();
    var url_header = $('#online_host').text().trim();
    var exerciseid = $('#online_exercise').val().trim();
    var courseid = $('#online_coursePackage').val().trim();

    var SAVEDATA = [];
    var requestData = {
        "username": username,
        "password": password,
        "url_header": url_header,
        "exerciseid": exerciseid,
        "courseid": courseid,
        "env": env
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/publish",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {
            alert("send  " + JSON.stringify(data));
        },
        error: function (e) {
            alert("send fail" + JSON.stringify(e));
        }

    });


};