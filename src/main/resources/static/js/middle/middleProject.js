var addObj;

//添加项目模态框
function showAddProject(obj) {
    addObj = obj;
    $("#addModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//添加项目
function addProject() {

    var name = $.trim($('#name').val());
    var requestPre = $.trim($('#requestPre').val());
    var runType = $.trim($('#runType').val());
    var loginType = $.trim($('#loginType').val());
    var loginUrl = $.trim($('#loginUrl').val());
    var loginParam = $.trim($('#loginParam').val());
    var secretUrl = $.trim($('#secretUrl').val());

    if (!name) {
        alert("请输入项目名");
        return false;
    }
    if (!requestPre) {
        alert("请输入请求ID前缀");
        return false;
    }
    var SAVEDATA = [];
    var requestData = {
        "name": name,
        "requestPre": requestPre,
        "runType": runType,
        "loginType": loginType,
        "loginUrl": loginUrl,
        "loginParam": loginParam,
        "secretUrl": secretUrl,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/middle/addMiddleProject",
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
        url: "/middle/deleteMiddleProject",
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

function showProjectDetail() {

}


var updateObj;

//给更新模块模态框传值
function showUpdateProject(obj) {
    updateObj = obj;
    var id = $(obj).attr("data-id");
    var name = $(obj).attr("data-name");
    $("#updateID").val(id);
    $("#updateProjectName").val(name);

    $("#updateModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//更新项目
function updateProjectModel() {

    var name = $('#updateProjectName').val().trim();
    var id = $('#updateID').val().trim();
    var SAVEDATA = [];


    if (!name) {
        alert("请输入模块名");
        return false;
    }
    var requestData = {
        "id": id,
        "name": name,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/middle/updateMiddleProject",
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


    var url = "/middle/project/getlist?currentPage=" + currentPage + "&pageSize=10";

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


function content(middleProject) {

    var contents = "";

    for (var i = 0; i < middleProject.length; i++) {


        contents += "  <tr><td>" + middleProject[i].id + "</td>\n" +
            "                    <td>" + middleProject[i].name + "</td>\n" +
            "                    <td>" + middleProject[i].loginTypeName + "</td>\n" +
            "                    <td>" + middleProject[i].runTypeName + "</td>\n" +
            "                    <td>\n" +
            "                        <a class=\"btn btn-sm btn-success\" href=\"/middle//project/detail/" + middleProject[i].id + "\">详情</a>\n" +
            "                        <a class=\"btn btn-sm btn-danger\" href=\"javascript:void(0)\" th:data-id=\'" + middleProject[i].id + "\' \n" +
            "                           onclick=\"showDeleteModal(this)\">删除</a>\n" +
            "                    </td></tr>"

    }
    return contents;
}


