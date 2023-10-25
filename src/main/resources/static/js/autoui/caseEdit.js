var addObj;

//添加模块模态框
function showAddModal(obj) {
    addObj = obj;
    $("#addModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//添加用例
function addUiCase() {
    var CONTENT = $('#addModelContent').val().trim();
    var NAME = $('#addModelName').val().trim();
    var COMMENTS = $('#addModelComments').val().trim();


    if (!CONTENT) {
        alert("请输入用例中文名称");
        return false;
    }
    if (!NAME) {
        alert("请输入用例内容");
        return false;
    }
    if (!COMMENTS) {
        alert("请输入用例描述");
        return false;
    }

    var SAVEDATA = [];
    var requestData = {
        "caseContent": CONTENT,
        "caseName": NAME,
        "caseDesc": COMMENTS,
    };

    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/case/insert",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 6000,
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
            alert("添加失败：" + JSON.stringify(e));
        }
    });
    var caseDesc = COMMENTS.replace(/#/g, "%23");

    // 添加本地文件
    var insertUrl = "http://okay-qaclient.xk12.cn/api/insertCase?caseName=" + NAME + "&caseDesc=" + caseDesc;
    $.ajax({
        type: "GET",
        url: insertUrl,
        dataType: "json",
        contentType: "application/json",
        timeout: 6000,
        success: function (data) {

            if (data.code == 200) {

            } else {
                alert("本地文件添加失败：" + data);
            }

        },
        error: function (e) {
            alert("本地文件添加失败：" + JSON.stringify(e));
        }
    });

};


var delObj;
var caseName;

//给确认删除模态框传值
function showDeleteModal(obj) {
    delObj = obj;
    var id = $(obj).attr("data-id");
    caseName = $(obj).attr("data-caseName");
    $("#deleteID").val(id);
    $("#deleteModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//删除模块
function deleteInfo() {

    var deleteId = $('#deleteID').val();
    var SAVEDATA = [];
    var requestData = {
        "id": deleteId,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/case/delete",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 6000,
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
            alert("删除失败：" + JSON.stringify(e));
        }
    });


    // 删除本地文件
    var deleteUrl = "http://okay-qaclient.xk12.cn/api/deleteCase?caseName=" + caseName;

    $.ajax({
        type: "GET",
        url: deleteUrl,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {

            } else {
                alert("本地文件删除失败:" + data);
            }

        },
        error: function (e) {
            alert("本地文件删除失败:" + JSON.stringify(e));
        }
    });

};


var updateObj;

//给更新模块模态框传值
function showUpdateModal(obj) {
    updateObj = obj;
    var id = $(obj).attr("data-id");
    var content = $(obj).attr("data-content");
    var name = $(obj).attr("data-name");
    var desc = $(obj).attr("data-comments");

    $("#updateID").val(id);
    $("#updateModelContent").val(content);
    $("#updateModelName").val(name);
    $("#updateModelDesc").val(desc);

    $("#updateModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//更新case
function updateCase() {

    var content = $("#updateModelContent").val().trim();
    ;
    var name = $('#updateModelName').val().trim();
    ;
    var desc = $('#updateModelDesc').val().trim();
    ;
    var id = $('#updateID').val().trim();
    ;
    var SAVEDATA = [];

    if (!content) {
        alert("请输入用例描述");
        return false;
    }
    if (!name) {
        alert("请输入用例名称");
        return false;
    }
    if (!desc) {
        alert("请输入用例详情");
        return false;
    }
    var requestData = {
        "id": id,
        "caseContent": content,
        "caseName": name,
        "caseDesc": desc,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/case/update",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 6000,
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
            alert("修改失败：" + JSON.stringify(e));
        }
    });
    var caseDesc = desc.replace(/#/g, "%23");

    // 更新本地文件
    var updateUrl = "http://okay-qaclient.xk12.cn/api/insertCase?caseName=" + name + "&caseDesc=" + caseDesc;

    $.ajax({
        type: "GET",
        url: updateUrl,
        dataType: "json",
        contentType: "application/json",
        timeout: 6000,
        success: function (data) {

            if (data.code == 200) {

            } else {
                alert("本地文件更新失败：" + data);
            }

        },
        error: function (e) {
            alert("本地文件更新失败：" + JSON.stringify(e));
        }
    });

};


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




// 更新分页的数据
function updatePageData() {

    var url = "/uiPad/case/getlist?currentPage=" + currentPage + "&pageSize=20";

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

function content(uicase) {

    var contents = "";

    for (var i = 0; i < uicase.length; i++) {

        contents += "<tr><td>" + uicase[i].id + "</td>\n" +
            "                    <td>" + uicase[i].caseContent + "</td>\n" +
            "                    <td>" + uicase[i].caseName + "</td>\n" +
            "                    <td>" + uicase[i].caseDesc + "</td>\n" +
            "                    <td>\n" +
            "                        <a href=\"javascript:void(0)\" data-id=\'" + uicase[i].id + "\'\n" +
            "                           data-content=\'" + uicase[i].caseContent + "\'\n" +
            "                           data-name=\'" + uicase[i].caseName +  "\'\n" +
            "                           data-comments=\'" + uicase[i].caseDesc + "\'\n" +
            "                           class=\"btn  btn-sm btn-info \"\n" +
            "                           onclick=\"showUpdateModal(this)\">修改</a>\n" +
            "\n" +
            "                        <a href=\"javascript:void(0)\" data-id=\'" + uicase[i].id + "\'\n" +
            "                           data-caseName=\'" + uicase[i].caseName + "\'\n" +
            "                           class=\"btn btn-sm btn-danger\"\n" +
            "                           onclick=\"showDeleteModal(this)\">删除</a>\n" +
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

    var url = "/uiPad/findCaseByName?caseName=" + caseName;

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