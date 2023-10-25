function showAddModal(obj) {

    $("#addModalWebUi").modal({
        backdrop: 'static',
        keyboard: false
    });
}


//显示大图
function showImage(source) {
    src = source.getAttribute("src");
    $("#ShowImage_Form").find("#img_show").html("<image src='" + src + "' class='carousel-inner img-responsive img-rounded' />");
    $("#ShowImage_Form").modal();
}


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


//给确认删除模态框传值
function showExcuteModalSingle(obj) {
    delObj = obj;
    var id = $(obj).attr("data-id");
    $("#excuteID").val(id);
    $("#excuteModal").modal({
        backdrop: 'static',
        keyboard: false
    });
}


function excuteInfo() {
    if ($("#excutePlatformId").val()!=null) {
        $.ajax({
            url: "/ui/executeCaseList",
            data: "platformId=" + $("#excutePlatformId").val()+"&caseDesc"+$("excuteCaseDesc").val()+"&testStatus"+$("excuteTestStatus").val(),
            type: "post",
            success: function (data) {
                $("#excuteModal").modal('hide');
                if (data.code == 200) {
                    window.location.reload();
                } else {
                    toastr.info(data.msg);
                }
            },
            error: function (data) {
                $("#excuteModal").modal('hide');
                toastr.info("网络异常，请重新操作");
            }

        });
    } else {
        $.ajax({
            url: "/ui/executeCaseSingle",
            data: "id=" + $("#excuteID").val(),
            type: "post",
            success: function (data) {
                $("#excuteModal").modal('hide');
                if (data.code == 200) {
                    window.location.reload();
                } else {
                    toastr.info(data.msg);
                }
            },
            error: function (data) {
                $("#excuteModal").modal('hide');
                toastr.info("网络异常，请重新操作");
            }

        });
    }

}


function deleteInfo() {
    $.ajax({
        url: "/ui/deleteWebCase",
        data: "id=" + $("#deleteID").val(),
        type: "post",
        success: function (data) {
            $("#deleteModal").modal('hide');
            if (data.code == 200) {
                window.location.reload();
            } else {
                toastr.info(data.msg);
            }

        },
        error: function (data) {
            $("#deleteModal").modal('hide');
            toastr.info("网络异常，请重新操作");

        }

    });
}

/**
 * 查询数据
 */
function selectCaseList() {
    var url = "/ui/webUiCaseList";
    var platformId = $("#platformId").val() > 0 ? $("#platformId").val() : ""
    var caseDesc = $("#caseDesc").val()
    var testStatus = $("#testStatus").val() >= 0 ? $("#testStatus").val() : ""
    url = url + "?platformId=" + platformId + "&caseDesc=" + caseDesc + "&testStatus=" + testStatus
    window.location.href = url
}

//展示
function showExcuteModal() {
    var platformId = $("#platformId").val() > 0 ? $("#platformId").val() : ""
    var caseDesc = $("#caseDesc").val()
    var testStatus = $("#testStatus").val() >= 0 ? $("#testStatus").val() : ""
    var platformIdHtml = "<input  class='form-control' type='hidden' id='excutePlatformId' value='" + platformId + "'/>"
    var caseDescHtml = "<input  class='form-control' type='hidden' id='excuteCaseDesc' value='" + caseDesc + "'/>"
    var testStatusHtml = "<input  class='form-control' type='hidden' id='excuteTestStatus' value='" + testStatus + "'/>"
    if ($("#excutePlatformId").val()) {
        $("#excutePlatformId").val(platformId)
    } else {
        $("#excuteModal").append(platformIdHtml)
    }
    if ($("#excuteCaseDesc").val()) {
        $("#excuteCaseDesc").val(caseDesc)
    } else {
        $("#excuteModal").append(caseDescHtml)
    }
    if ($("#excuteTestStatus").val()) {
        $("#excuteTestStatus").val(testStatus)
    } else {
        $("#excuteModal").append(testStatusHtml)
    }

    $("#excuteModal").modal({
        backdrop: 'static',
        keyboard: false
    });
}


$(function () {
    toastr.options = { // toastr配置
        "closeButton": false, //是否显示关闭按钮
        "debug": false, //是否使用debug模式
        "progressBar": false, //是否显示进度条，当为false时候不显示；当为true时候，显示进度条，当进度条缩短到0时候，消息通知弹窗消失
        "positionClass": "toast-top-center",//显示的动画时间
        "showDuration": "300", //显示的动画时间
        "hideDuration": "100", //消失的动画时间
        "timeOut": "1000", //展现时间
        "extendedTimeOut": "1000", //加长展示时间
        "showEasing": "swing", //显示时的动画缓冲方式
        "hideEasing": "linear", //消失时的动画缓冲方式
        "showMethod": "fadeIn", //显示时的动画方式
        "hideMethod": "fadeOut" //消失时的动画方式
    }
});