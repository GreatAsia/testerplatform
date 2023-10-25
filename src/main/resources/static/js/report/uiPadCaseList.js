
function catLog(obj) {

    var logs = $(obj).attr("data-log");
    $("#logDetail").text(logs);
    //显示模态框
    $("#catLog").modal({
        backdrop: 'static',
        keyboard: false
    });

}



function catPicture(obj) {

    var pictures = $(obj).attr("data-picture");
    var paths = pictures.split(",");
    var length = paths.length;
    var text = " <pre style=\"width:100%;\" rows=\"20\" >\n";
    for(var i = 0;i<length;i++){
        text += "<img src=\"/img/" + paths[i].replace("]","").replace("[","") + "\"  height=\"300\"  width=\"450\">\n"
    }
    text += "</pre>";
    $("#picture-content").html(text);
    //显示模态框
    $("#catPicture").modal({
        backdrop: 'static',
        keyboard: false
    });

}


function catTestLog(obj) {

    var logs = $(obj).attr("data-log");
    var url = "/img/" + logs;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "text",
        contentType: "text/html",
        timeout: 60000,
        success: function (data) {

                $("#testLogDetail").text(data);
                //显示模态框
                $("#catTestLog").modal({
                    backdrop: 'static',
                    keyboard: false
                });

        },
        error: function (e) {
            alert("查看日志失败：" + JSON.stringify(e));
        }
    });



}

