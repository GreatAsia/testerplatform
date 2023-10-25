

function getDevList() {
   var address = $('#devHost').val().trim();
   var path = $('#devPath').val().trim();
    getList(address, path);
}


function getHotfixList() {
    var address = $('#hotfixHost').val().trim();
    var path = $('#hotfixPath').val().trim();
    getList(address, path);
}

function getStressList() {
    var address = $('#stressHost').text().trim();
    var path = $('#stressPath').text().trim();
    getList(address, path);
}


// 获取zk数据
function getList(address, path) {

    var url = "/tool/zk/list?address=" + address + "&path=" + path;

    //显示模态框
    $("#zkList").modal({
        backdrop: 'static',
        keyboard: false
    });
    $("#respose").val("请等待查询结果");
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 600000,
        success: function (data) {
            var json = JSON.stringify(data.data, null, 4);
            $("#respose").val(json);
        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e));
        }
    });

}


function getDevSearchList() {
    var address = $('#devHostSearch').val().trim();
    var path = $('#devPathSearch').val().trim();
    var find = $('#devContentSearch').val().trim();
    getSearchList(address, path, find);
}


function getHotfixSearchList() {
    var address = $('#hotfixHostSearch').val().trim();
    var path = $('#hotfixPathSearch').val().trim();
    var find = $('#hotfixContentSearch').val().trim();
    getSearchList(address, path, find);

}

function getStressSearchList() {
    var address = $('#stressHostSearch').val().trim();
    var path = $('#stressPathSearch').val().trim();
    var find = $('#stressContentSearch').val().trim();
    getSearchList(address, path, find);
}

// 获取zk数据
function getSearchList(address, path, find) {

    var url = "/tool/zk/find/list?address=" + address + "&path=" + path + "&findContent=" + find;

    //显示模态框
    $("#zkList").modal({
        backdrop: 'static',
        keyboard: false
    });
    $("#respose").val("请等待....");
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 600000,
        success: function (data) {
            console.log("data==" + data);
            var json = JSON.stringify(data.data, null, 4);
            $("#respose").val(json);

        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e));
        }
    });

}