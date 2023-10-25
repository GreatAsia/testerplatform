

// 切换项目List页面
function switchListProject() {

    var projectId = $('#projectCaseName').val();


    var url = "/middle/queryModuleByProject?project_id=" + projectId;

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {
            var str = "";
            for (var i = 0; i < data.length; i++) {

                str += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $("#moduleCaseName").html(str);


            var module_Id = $('#moduleCaseName').val();
            if (module_Id == null) {

                $("#interfaceCaseName").val("");
                $("#tbody").html("");
                return false;
            }


            var url = "/middle/queryInterfaceByModuleId?module_Id=" + module_Id;

            $.ajax({
                type: "GET",
                url: url,
                dataType: "json",
                contentType: "application/json",
                timeout: 60000,
                success: function (data) {
                    var str = "";
                    for (var i = 0; i < data.length; i++) {
                        str += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                    }

                    $("#interfaceCaseName").html(str);


                },
                error: function (e) {
                    alert("请求失败：" + e);
                }
            });


        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });


}

// 切换模块List页面
function switchListModule() {


    var module_Id = $('#moduleCaseName').val();

    if (module_Id == null) {

        $("#interfaceCaseName").val("");
        $("#tbody").html("");
    }


    var url = "/middle/queryInterfaceByModuleId?module_Id=" + module_Id;

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {
            var str = "";
            for (var i = 0; i < data.length; i++) {

                str += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $("#interfaceCaseName").html(str);


        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });


}


// 按项目运行用例
function runProject() {

    var project_id = $('#projectCaseName').val();
    var env_id = $('#SelectMiddleCaseEnv').val();
    var type = $('#middleCaseType').val();

    if (env_id == null) {
        alert("env_id=" + env_id);
        return;
    }

    if (project_id == null) {
        alert("priject_id=" + project_id);
        return;
    }

    url = "/middle/runMiddleProjectByType?env_id=" + env_id + "&project_id=" + project_id + "&type=" + type;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        async: true,
        timeout: 600000,
        success: function (data) {
            alert("正在运行，请稍后查看结果")
        },
        error: function (e) {
            alert("run fail:" + JSON.stringify(e, null, 4));
        }

    });


}

function runModule() {
    var module_id = $('#moduleCaseName').val();
    var env_id = $('#SelectMiddleCaseEnv').val();
    var type = $('#middleCaseType').val();

    if (env_id == null) {
        alert("env_id=" + env_id);
        return;
    }

    if (module_id == null) {
        alert("module_id=" + module_id);
        return;
    }

    url = "/middle/runMiddleModuleByType?env_id=" + env_id + "&module_id=" + module_id + "&type=" + type;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        async: true,
        timeout: 600000,
        success: function (data) {
            alert("正在运行，请稍后查看结果")
        },
        error: function (e) {
            alert("run fail:" + JSON.stringify(e, null, 4));
        }

    });



}


function runInterface() {
    var interface_id = $('#interfaceCaseName').val();
    var env_id = $('#SelectMiddleCaseEnv').val();
    var type = $('#middleCaseType').val();

    if (env_id == null) {
        alert("env_id=" + env_id);
        return;
    }

    if (interface_id == null) {
        alert("interface_id=" + interface_id);
        return;
    }

    url = "/middle/runMiddleInterfaceByType?env_id=" + env_id + "&interface_id=" + interface_id + "&type=" + type;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        async: true,
        timeout: 600000,
        success: function (data) {
            alert("正在运行，请稍后查看结果")
        },
        error: function (e) {
            alert("run fail:" + JSON.stringify(e, null, 4));
        }

    });



}



