function sendRequest() {

    var ADDRESS = $.trim($('#address').find("option:selected").text());
    var PARAMTYPE = $.trim($('#addRequestType').val());
    var INTERFACECLASS = $.trim($('#addInterFaceClassName').val());
    var METHODNAME = $.trim($('#addMethodName').val());
    var PARAMS = $.trim($('#addParams').val());
    var VERSION = $.trim($('#addVersion').val());
    var url;

    if (!ADDRESS) {
        alert("请选择环境");
        return false;
    }
    if (!PARAMTYPE) {
        alert("请输入参数类型");
        return false;
    }
    if (!INTERFACECLASS) {
        alert("请输入接口类");
        return false;
    }
    if (!METHODNAME) {
        alert("请输入接口名");
        return false;
    }
    if (!PARAMS) {
        alert("请输入参数");
        return false;
    }

    if (!VERSION) {
        alert("请输入版本");
        return false;
    }
    var requestData = {

        "address": ADDRESS,
        "paramType": PARAMTYPE,
        "interfaceClass": INTERFACECLASS,
        "methodName": METHODNAME,
        "params": PARAMS,
        "version":VERSION,
    };
    url = "/dubbo/run";
    $.ajax({
        type: "POST",
        url: url,
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {
            // var json = JSON.stringify(data, null, 4);
            $('#result').val("");
            $('#result').html("<pre>" + syntaxHighlight(data) + "</pre>");
        },
        error: function (e) {
            var json = e.responseText;
            $('#result').val(json)
            alert("请求失败：" + e);
        }
    });

};

//添加用例
function addDubboCase() {

    var caseName = $.trim($('#addCaseName').val());
    var model = $.trim($('#addModelId').val());
    var interfaceClass = $.trim($('#addInterFaceClassName').val());
    var methodName = $.trim($('#addMethodName').val());
    var requestType = $.trim($('#addRequestType').val());
    var params = $.trim($('#addParams').val());
    var checkDate = $.trim($('#addCheckDate').val());
    var type = $.trim($('#addType').val());
    var version = $.trim($('#addVersion').val());
    var envId = $.trim($('#address').val());
    if (!caseName) {
        alert("请输入用例名");
        return false;
    }
    if (!model) {
        alert("请输入模块");
        return false;
    }
    if (!interfaceClass) {
        alert("请输入接口类型");
        return false;
    }
    if (!methodName) {
        alert("请输入方法名");
        return false;
    }
    if (!requestType) {
        alert("请输入请求类型");
        return false;
    }
    if (!params) {
        alert("请输入请求参数");
        return false;
    }
    if (!checkDate) {
        alert("请输入验证数据");
        return false;
    }

    if (!type) {
        alert("请输入用例类型");
        return false;
    }

    if (!version) {
        alert("请输入版本");
        return false;
    }

    var SAVEDATA = [];
    var requestData = {
        "envId": envId,
        "caseName": caseName,
        "model": model,
        "interFaceClassName": interfaceClass,
        "methodName": methodName,
        "requestType": requestType,
        "params": params,
        "checkData": checkDate,
        "type": type,
        "version":version,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/dubbo/interface/insert",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            var json = JSON.stringify(data, null, 4);
            if (data.code == 200) {
                alert("添加成功：");
            } else {
                alert("添加失败：" + json);
            }

        },
        error: function (e) {
            alert("添加失败：" + e);
        }
    });


};


//处理json数据，采用正则过滤出不同类型参数
function syntaxHighlight(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
};



