//请求数据
function catContent(obj) {

    $("#contentDetail").html("");
    var requestContent = $(obj).attr("data-content");
    $("#contentDetail").html(requestContent);
    //显示模态框
    $("#content").modal({
        backdrop: 'static',
        keyboard: false
    });

}

function createBug() {

    //显示模态框
    $("#bugInfo").modal({
        backdrop: 'static',
        keyboard: false
    });
}


function downLoadReport() {
    var url = document.location.toString().split("/");
    var historyId = url[url.length - 1].trim();
    window.location.href = "/middle/report/export?historyId=" + historyId;

}
