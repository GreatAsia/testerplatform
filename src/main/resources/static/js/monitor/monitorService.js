var addObj;

//添加模块模态框
function showAddModal(obj) {
    addObj = obj;
    $("#addModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//添加模块
function addService() {

    var project = $.trim($('#serviceName').val());
    var phone = $.trim($('#phoneList').val());
    var qa_name = $.trim($('#qaName').val());
    var rd_name = $.trim($('#rdName').val());
    if (!project) {
        alert("请输入服务名");
        return false;
    }
    if (!phone) {
        alert("请输入手机号");
        return false;
    }
    if (!qa_name) {
        alert("请输入测试人员");
        return false;
    }
    if (!rd_name) {
        alert("请输入开发人员");
        return false;
    }

    var SAVEDATA = [];
    var requestData = {
        "project": project,
        "phone": phone,
        "create_time": "",
        "update_time": "",
        "qa_name": qa_name,
        "rd_name": rd_name,
    };

    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/monitor/service/insert",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，刷新页面
                $("#addModal").modal('hide');
                location.reload();
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
        url: "/monitor/service/delete",
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


var updateObj;

//给更新模块模态框传值
function showUpdateModal(obj) {
    updateObj = obj;
    var id = $(obj).attr("data-id");
    var project = $(obj).attr("data-project");
    var phone = $(obj).attr("data-phone");
    var qaName = $(obj).attr("data-qa");
    var rdName = $(obj).attr("data-rd");

    $("#updateID").val(id);
    $("#updateServiceName").val(project);
    $("#updatePhoneList").val(phone);
    $("#updateQaName").val(qaName);
    $("#updateRdName").val(rdName);

    $("#updateModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//更新模块
function updateService() {
    var id = $('#updateID').val().trim();
    var project = $('#updateServiceName').val().trim();
    var phone = $('#updatePhoneList').val().trim();
    var updateTime = new Date().format("yyyy-MM-dd hh:mm:ss");
    var qa_name = $('#updateQaName').val().trim();
    var rd_name = $('#updateRdName').val().trim();

    if (!project) {
        alert("请输入项目名");
        return false;
    }
    if (!phone) {
        alert("请输入手机号");
        return false;
    }
    if (!qa_name) {
        alert("请输入测试人员");
        return false;
    }
    if (!rd_name) {
        alert("请输入开发人员");
        return false;
    }


    var requestData = {
        "id": id,
        "project": project,
        "phone": phone,
        "updateTime": "",
        "qa_name": qa_name,
        "rd_name": rd_name,
    };
    $.ajax({
        type: "POST",
        url: "/monitor/service/update",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，刷新页面
                $("#updateModal").modal('hide');
                location.reload();
            } else {
                alert("修改失败：" + data);
            }

        },
        error: function (e) {
            alert("修改失败：" + e);
        }
    });


};

Date.prototype.format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}