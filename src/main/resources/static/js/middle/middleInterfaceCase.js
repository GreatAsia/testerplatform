//添加用例模态框
function showAddInterfaceCase() {

    var env_id = $.trim($('#SelectMiddleCaseEnv').val());
    $("#addMiddleCaseEnv").val(env_id);


    switchProjectInAddCase();

    $('#addMiddleCaseName').val("");
    $('#addRequestData').val("");
    $('#addCheckData').val("");

    $("#addModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//添加用例
function addMiddleCase() {

    var env_id = $.trim($('#addMiddleCaseEnv').val());
    var interface_id = $.trim($('#addInterfaceName').val());
    var caseType = $.trim($('#addMiddleCaseType').val());
    var name = $.trim($('#addMiddleCaseName').val());
    var request_data = $.trim($('#addRequestData').val());
    var check_data = $.trim($('#addCheckData').val());

    if (!name) {
        alert("请输入用例名");
        return false;
    }

    if (!check_data) {
        alert("请输入校验数据");
        return false;
    }

    var SAVEDATA = [];
    var requestData = {
        "env_id": env_id,
        "interface_id": interface_id,
        "caseType": caseType,
        "name": name,
        "request_data": request_data,
        "check_data": check_data,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/middle/addMiddleCase",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，刷新页面
                $("#addModal").modal('hide');
                updatePageData();
                lastPage();
            } else {
                alert("添加失败：" + data);
            }

        },
        error: function (e) {
            alert("添加失败：" + e);
        }
    });

};


var delObj;

//给确认删除模态框传值
function showDeleteModal(obj) {
    delObj = obj;
    var id = $(obj).attr("data-id");
    $("#deleteID").val(id);
    $("#deleteModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}


//删除项目
function deleteInfo() {

    var deleteId = $('#deleteID').val();
    var SAVEDATA = [];
    var requestData = {
        "id": deleteId,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/middle/deleteMiddleCase",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，删除对应的行
                $("#deleteModal").modal('hide');
                $(delObj).parents("tr").remove()
            } else {
                alert("删除失败：" + data);
            }

        },
        error: function (e) {
            alert("删除失败：" + e);
        }
    });

};


//给更新接口模态框传值
function showUpdateModule(obj) {

    var id = $(obj).attr("data-id");
    var env_id = $(obj).attr("data-env_id");
    var interface_id = $(obj).attr("data-interface_id");
    var caseType = $(obj).attr("data-caseType");
    var name = $(obj).attr("data-name");
    var request_data = $(obj).attr("data-request_data");
    var check_data = $(obj).attr("data-check_data");
    var project_id = $(obj).attr("data-project_id");
    var module_id = $(obj).attr("data-module_id");


    $("#updateCaseProject").val(project_id);
    updateModuleName(project_id, module_id, interface_id);

    $("#updaeCaseEnv").val(env_id);
    $("#updateCaseId").val(id);
    $("#updateCaseType").val(caseType);
    $("#updateCaseName").val(name);
    $("#updateCaseRequestData").val(request_data);
    $("#updateCaseCheckData").val(check_data);


    $("#updateModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

// 修改接口用例
function updateInterfaceCase() {

    var id = $("#updateCaseId").val().trim();
    var env_id = $("#updaeCaseEnv").val().trim();
    var interface_id = $("#updateCaseInterface").val().trim();
    var caseType = $("#updateCaseType").val().trim();
    var name = $("#updateCaseName").val().trim();
    var request_data = $("#updateCaseRequestData").val().trim();
    var check_data = $("#updateCaseCheckData").val().trim();

    var SAVEDATA = [];

    if (!name) {
        alert("请输入用例名");
        return false;
    }


    if (!check_data) {
        alert("请输入校验数据");
        return false;
    }

    var requestData = {
        "id": id,
        "env_id": env_id,
        "interface_id": interface_id,
        "caseType": caseType,
        "name": name,
        "request_data": request_data,
        "check_data": check_data,
    };
    SAVEDATA.push(requestData);
    console.log("上行=" + JSON.stringify(requestData));
    $.ajax({
        type: "POST",
        url: "/middle/updateMiddleCase",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，刷新页面
                $("#updateModal").modal('hide');
                updatePageData();
            } else {
                alert("修改失败：" + data);
            }

        },
        error: function (e) {
            alert("修改失败：" + e);
        }
    });


};


//添加用例页显示切换模块页面
function switchProjectInAddCase() {

    var projectId = $('#projectCaseName').val();
    var moduleCaseName = $('#moduleCaseName').val();

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

            $("#addProjectName").val(projectId);
            $("#addModuleName").html(str);
            $("#addModuleName").val(moduleCaseName);
            switchModuleInAddCase();


        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });

}

//添加用例模态框选择接口
function switchModuleInAddCase() {

    //添加接口选择框
    var module_Id = $('#moduleCaseName').val();
    var interface = $('#interfaceCaseName').val();

    if (module_Id == null) {

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
            $("#addInterfaceName").html(str);
            $("#addInterfaceName").val(interface);

        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });


}


// 切换List页面项目
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

                    switchListEnvAndInterface();

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

// 切换List页面模块
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
            // 更新case列表
            switchListEnvAndInterface();

        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });


}

// 根据项目Id填充模块和接口
function updateModuleName(project_id, module_id, interface_id) {


    var url = "/middle/queryModuleByProject?project_id=" + project_id;

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {
            var str = "";
            for (var i = 0; i < data.length; i++) {

                if (data[i].id == module_id) {
                    str += '<option value="' + data[i].id + '" + selected>' + data[i].name + '</option>';
                    continue;
                }


                str += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $("#updateCaseModule").html(str);

            //更新case模态框接口填充

            var url = "/middle/queryInterfaceByModuleId?module_Id=" + module_id;

            $.ajax({
                type: "GET",
                url: url,
                dataType: "json",
                contentType: "application/json",
                timeout: 60000,
                success: function (data) {
                    var str = "";
                    for (var i = 0; i < data.length; i++) {

                        if (data[i].id == interface_id) {
                            str += '<option value="' + data[i].id + '" + selected>' + data[i].name + '</option>';
                            continue;
                        }
                        str += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                    }

                    $("#updateCaseInterface").html(str);


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


//切换修改用例的项目

function switchUpdateModuleName() {

    var projectId = $('#updateCaseProject').val();

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

            $("#updateCaseModule").html(str);


        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });


}

// 切换修改用例的模块
function switchUpdateInterfaceName() {

    var module_Id = $('#updateCaseModule').val();

    if (module_Id == null) {
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
            $("#updateCaseInterface").html(str);


        },
        error: function (e) {
            alert("请求失败：" + e);
        }
    });


}


//给运行case模态框传值
function runCaseModal(obj) {
    runObj = obj;
    var id = $(obj).attr("data-id");
    url = "/middle/runMiddleSingle?id=" + id;
    $("#response").val("运行中...");


    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        async: true,
        timeout: 60000,
        success: function (data) {
            $("#result").val(data.result);
            $("#response").val(data.response);
            //显示模态框
            $("#runCase").modal({
                backdrop: 'static',
                keyboard: false
            });
        },
        error: function (e) {
            $("#response").val(e.responseText);
            $("#result").val("FAIL");
            //显示模态框
            $("#runCase").modal({
                backdrop: 'static',
                keyboard: false
            });
        }

    });
}



function exportModule() {
    var module_id = $('#moduleCaseName').val();
    var env_id = $('#SelectMiddleCaseEnv').val();
    var projectName = $.trim($('#projectCaseName').find("option:selected").text());

    if (env_id == null) {
        alert("env_id=" + env_id);
        return;
    }

    if (module_id == null) {
        alert("module_id=" + module_id);
        return;
    }
    window.location.href = "/middle/case/module/export?projectName=" + projectName + "&envId=" + env_id + "&moduleId=" + module_id;

}

function importModule() {
    $("#importExcel").modal({
        backdrop: 'static',
        keyboard: false
    });


}


// 切换List页面环境和接口
function switchListEnvAndInterface() {

    currentPage = 1;
    updatePageData();

}


var currentPage = 1;
var totalPage = 1;


// 首页
function homePage() {

    if (currentPage == 1) {
        return
    }
    currentPage = 1;
    updatePageData();

}


// 末页
function lastPage() {

    totalPage = parseInt($("#totalPage").text().replace("共", "").replace("页", ""));
    if (currentPage == totalPage) {
        return
    }

    currentPage = totalPage;
    updatePageData();

}


// 下一页
function nextPage() {
    totalPage = parseInt($("#totalPage").text().replace("共", "").replace("页", ""));
    currentPage = currentPage + 1;

    if (currentPage > totalPage) {
        currentPage = currentPage - 1;

        return;
    }

    updatePageData();

}

// 上一页
function previousPage() {

    currentPage = currentPage - 1;
    if (currentPage <= 0) {
        currentPage = currentPage + 1;
        return;
    }
    updatePageData();

}


// 切换模块
function switchMiddleModule() {
    currentPage = 1;
    updatePageData();

}


// 更新分页的数据
function updatePageData() {

    var env_id = $("#SelectMiddleCaseEnv").val();
    var interface_id = $("#interfaceCaseName").val();

    if (interface_id == null) {
        $("#tbody").html("");
        return;
    }

    var url = "/middle/case/getlist?env_id=" + env_id + "&interface_id=" + interface_id + "&currentPage=" + currentPage + "&pageSize=10";

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {

            $("#tbody").html(content(data.list));
            totalPage = data.pages;

            if (data.list.length == 0) {
                $("#currentPage").text("第0页");
            } else {
                $("#currentPage").text("第" + currentPage + "页");
            }

            $("#totalPage").text("共" + totalPage + "页");
        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e));
        }
    });

}

function content(middleCase) {

    var contents = "";

    for (var i = 0; i < middleCase.length; i++) {

        var caseType;

        if(middleCase[i].caseType == 0){
            caseType = "监控";
        }else {
            caseType = "手动";
        }


        contents += "<tr><td>" + middleCase[i].id + "</td>\n" +
            "                    <td>" + middleCase[i].envName + "</td>\n" +
            "                    <td>" + middleCase[i].interfaceName + "</td>\n" +
            "                    <td>" + middleCase[i].name + "</td>\n" +
            "                    <td>" + middleCase[i].request_data + "</td>\n" +
            "                    <td>" + middleCase[i].check_data + "</td>\n" +
            "                    <td>" + caseType + "</td>\n" +
            "                    <td>\n" +
            "                        <a href=\"javascript:void(0)\" data-id=\'" + middleCase[i].id + "\' class=\"btn btn-sm btn-success\"\n" +
            "                           onclick=\"runCaseModal(this)\">运行</a>\n" +
            "                        <a href=\"javascript:void(0)\"\n" +
            "                           data-id=\'" + middleCase[i].id + "\'\n" +
            "                           data-env_id=\'" + middleCase[i].env_id + "\'\n" +
            "                           data-interface_id=\'" + middleCase[i].interface_id + "\'\n" +
            "                           data-caseType=\'" + middleCase[i].caseType + "\'\n" +
            "                           data-name=\'" + middleCase[i].name + "\'\n" +
            "                           data-request_data=\'" + middleCase[i].request_data + "\'\n" +
            "                           data-check_data=\'" + middleCase[i].check_data + "\'\n" +
            "                           data-module_id=\'" + middleCase[i].module_id + "\'\n" +
            "                           data-project_id=\'" + middleCase[i].project_id + "\'\n" +
            "                           class=\"btn  btn-sm btn-info \"\n" +
            "                           onclick=\"showUpdateModule(this)\">修改</a>\n" +
            "\n" +
            "                        <a href=\"javascript:void(0)\" data-id=\'" + middleCase[i].id + "\' class=\"btn btn-sm btn-danger\"\n" +
            "                           onclick=\"showDeleteModal(this)\">删除</a>\n" +
            "                        <a  href=\'/middle/param/rule?id=" + middleCase[i].id + "\' data-id=\'" + middleCase[i].id + "\' class=\"btn btn-sm btn-success\"\n" +
            "                           >生成案例</a>\n" +
            "                    </td></tr>"

    }

    return contents;


}


function search() {

    var caseName = $("#searchName").val().trim();

    if (caseName == null || caseName == "" || caseName == undefined) {
        alert("请输入用例名称");
        return
    }

    var url = "/middle/queryCaseByName?caseName=" + caseName;

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {

            $("#tbody").html(content(data));

            currentPage = 1;
            totalPage = 1;
            $("#currentPage").text("第1页");
            $("#totalPage").text("共1页");
        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e));
        }
    });


}


document.onkeydown = function (e) {
    var ev = document.all ? window.event : e;
    if (ev.keyCode == 13) {
        search();
    }
}