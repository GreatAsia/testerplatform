function addUiCase() {
    //组装json数据
    platformId = $("[name='platformId']").val();
    if (platformId == null || platformId == 0) {
        /*alert("平台不能为空")*/
        toastr.error("平台不能为空");
        return false
    }
    url = $("[name='url']").val();
    if (url == null || url == "") {
        toastr.error("url不能为空");
        //alert("用例描述不能为空")
        return false
    }

    needLogin = $("[name='needLogin']").val();
    if (needLogin == null || needLogin == "") {
        toastr.error("是否登录案例不能为空");
        return false
    }

    caseDesc = $("[name='caseDesc']").val();

    var caseStepArray = document.getElementsByName('caseStep');
    var caseStepArrayJson = new Array();
    for (i = 0; i < caseStepArray.length; i++) {
        var caseStepJson = new Map();
        var selectStep = caseStepArray[i].querySelector("select[name='selectStep']").value;
        /*if (selectStep == null || selectStep == 0) {
            toastr.error("步骤" + (i + 1) + "的选择器不能为空");
            return false
        }*/
        var stepContent = caseStepArray[i].querySelector("input[name='stepContent']").value
        /*if (stepContent == null || stepContent == "") {
            toastr.error("步骤" + (i + 1) + "的用例描述不能为空");
            return false
        }*/
        if ((stepContent == null || stepContent == "") && selectStep > 0) {
            toastr.error("步骤" + (i + 1) + "的用例操作内容不能为空");
            return false
        }
        if ((stepContent != "") && selectStep == 0) {
            toastr.error("步骤" + (i + 1) + "的选择器不能为空");
            return false
        }

        var operationStep = caseStepArray[i].querySelector("select[name='operationStep']").value
        var operationContent = caseStepArray[i].querySelector("input[name='operationContent']").value
        if (operationStep == 0 && selectStep > 0) {
            toastr.error("步骤" + (i + 1) + "操作方式填写不正确");
            return false
        }

        if (operationStep > 2 && operationContent == "") {
            toastr.error("步骤" + (i + 1) + "操作内容填写不正确");
            return false
        }

        if (operationStep == 1) {
            caseStepJson.set('operationStep', operationStep);
        }

        if (operationStep > 1) {
            caseStepJson.set('operationStep', operationStep);
            caseStepJson.set('operationContent', operationContent);
        }


        var assertStep = caseStepArray[i].querySelector("select[name='assertStep']").value
        var assertType = caseStepArray[i].querySelector("select[name='assertType']").value
        var assertContent = caseStepArray[i].querySelector("input[name='assertContent']").value

        if (assertStep > 0 && assertType > 0 && assertContent != "") {
        } else {
            if (assertStep == 0) {
                toastr.error("步骤" + (i + 1) + "断言方式填写不正确");
                return false
            } else if (assertType == 0) {
                toastr.error("步骤" + (i + 1) + "断言类型名称不正确");
                return false
            } else if (assertContent == null || assertContent == "") {
                toastr.error("步骤" + (i + 1) + "断言内容填写不正确");
                return false
            }
        }


        caseStepJson.set('selectStep', selectStep);
        caseStepJson.set('stepContent', stepContent);

        if (assertStep != 0) {
            caseStepJson.set('assertStep', assertStep);
            caseStepJson.set('assertType', assertType);
            caseStepJson.set('assertContent', assertContent);
        }
        caseStepArrayJson.push(_strMapToObj(caseStepJson))
    }
    var addCaseJson = new Map();
    addCaseJson.set("platformId", platformId)
    addCaseJson.set("url", url);
    addCaseJson.set("caseDesc", caseDesc);
    addCaseJson.set("needLogin", needLogin);
    addCaseJson.set("caseStep", JSON.stringify(caseStepArrayJson))

    var data = ""
    for (var [key, value] of addCaseJson) {
        if (data == "") {
            data += (key + "=" + value)
        } else {
            data = data + "&" + (key + "=" + value)
        }

    }

    if (window.location.href.indexOf("update") != -1) {
        data = data + "&" + window.location.search.substring(1);
    }

    /*var params=JSON.stringify(_strMapToObj(addCaseJson))*/

    $.ajax({

        url: "/ui/addWebUiCase",

        data: data,

        type: "post",

        dataType: "json", //jsonp会把请求类型强制转换为get请求

        /*contentType: false,*/

        //contentType: "application/x-www-form-urlencoded",

        processData: false,

        async: true, //异步请求

        cache: false, //是否缓存

        /*beforeSend: function() {

            toastr.info("正在添加");

        },*/

        success: function (data) {
            if (data.code == 200) {
                /*url=request.getHeader("Referer");*/

                window.location.href = document.referrer;
            } else {
                toastr.info(data.msg);
            }

        },

        error: function (data) {
            toastr.info("网络异常，请重新操作");

        }

    });
    return false;
}


function _strMapToObj(strMap) {
    let obj = Object.create(null);
    for (let [k, v] of strMap) {
        obj[k] = v;
    }
    return obj;
}


function addStep() {
    var caseStepArrayOld = document.getElementsByName("caseStep");
    caseStepArrayOld[caseStepArrayOld.length - 1].insertAdjacentHTML('afterend', caseStepArrayOld[caseStepArrayOld.length - 1].outerHTML)
    var caseStepArray = document.getElementsByName("caseStep");
    var caseStepLast = caseStepArray[caseStepArray.length - 1]
    var stepNameElement = caseStepLast.querySelector("label[name='stepName']")
    stepNameElement.innerHTML = "步骤" + (caseStepArray.length)
    var assertNameElement = caseStepLast.querySelector("label[name='operationName']")
    assertNameElement.innerHTML = "操作" + (caseStepArray.length)
    var assertNameElement = caseStepLast.querySelector("label[name='assertName']")
    assertNameElement.innerHTML = "断言" + (caseStepArray.length)
}

function removeLastStep() {
    var caseStepArrayOld = document.getElementsByName("caseStep");
    if (caseStepArrayOld.length == 1) {
        toastr.error("只剩下最后一条数据了");
        return false
    }
    caseStepArrayOld[caseStepArrayOld.length - 1].remove()
}

function operationFunc(obj) {
    var selectVal = $(obj).children().val()
    if (selectVal < 3) {
        $(obj).parent().find("input").attr("disabled", true)
    } else {
        $(obj).parent().find("input").attr("disabled", false)
    }
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


//-------------------以下内容是页面加载时运行的-------------------
window.onload = function () {
    //方法内容
    addPlatformOptions()
    addSteps()
    addOperation()
    addAssert()
    if (window.location.href.indexOf("update") != -1) {
        addUpdateMsg()
    }


}


/**
 * 平台
 */
function addPlatformOptions() {
    $("#platformId").empty();
    $.ajax({
        url: "/ui/getPlatformById",
        type: "get",
        dataType: "json",
        success: function (data) {
            $("#platformId").append("<option value='0'>请选择</option>")
            for (var i = 0; i < data.data.length; i++) {
                $("#platformId").append("<option value=" + data.data[i].id + ">" + data.data[i].platformName + "</option>")
            }
        },
        error: function (data) {
            toastr.info(data.msg);
        },
        async: false
    })
}


/**
 * 步骤
 */
function addSteps() {
    addSteps = $.ajax({
        url: "/ui/getStep",
        type: "get",
        dataType: "json",
        success: function (data) {
            for (var key in data.data) {
                $("#selectStep").append("<option value=" + key + ">" + data.data[key] + "</option>")
            }
        },
        error: function (data) {
            toastr.info(data.msg);
        }
    })
    return addSteps
}


/**
 * 步骤
 */
function addOperation() {
    addOperation = $.ajax({
        url: "/ui/getOperation",
        type: "get",
        dataType: "json",
        success: function (data) {
            for (var key in data.data) {
                $("#operationStep").append("<option value=" + key + ">" + data.data[key] + "</option>")
            }
        },
        error: function (data) {
            toastr.info(data.msg);
        }
    })
    return addOperation
}

/**
 * 断言
 */
function addAssert() {
    addAssert = $.ajax({
        url: "/ui/getAssertType",
        type: "post",
        dataType: "json",
        success: function (data) {
            for (var i = 0; i < data.data.length; i++) {
                $("#assertStep").append("<option value=" + data.data[i].id + ">" + data.data[i].name + "</option>")
            }
        },
        error: function (data) {
            toastr.info(data.msg);
        }
    })
    return addAssert
}


function changeAssertType(dom) {
    var params = "assertTypeId=" + dom.value
    $.ajax({
        url: "/ui/getAssertTypeName",
        type: "post",
        data: params,
        dataType: "json",
        success: function (data) {
            var assertType = $(dom).parent().find("#assertType")
            assertType.empty();
            assertType.append("<option value='0'>断言类型方式</option>")
            if (dom.value > 0) {
                for (var i = 0; i < data.data.length; i++) {
                    assertType.append("<option value=" + data.data[i].id + ">" + data.data[i].name + "</option>")
                }
            }
        },
        error: function (data) {
            toastr.info(data.msg);
        }
    })
}


function addUpdateMsg() {

    $.ajax({
        url: "/ui/getUpdateCase?" + window.location.search.substring(1),
        type: "post",
        //data: params,
        dataType: "json",
        success: function (data) {
            $("#platformId option[value='" + data.data.platformId + "']").attr("selected", true)
            $("#needLogin option[value='" + data.data.needLogin + "']").attr("selected", true)
            $("input[name='caseDesc']").val(data.data.caseDesc)
            $("input[name='url']").val(data.data.url)
            var caseStep = JSON.parse(data.data.caseStep)
            for (var i = 0; i < caseStep.length; i++) {
                if (i > 0) {
                    addStep();
                }
                var caseStepArrayOld = document.getElementsByName("caseStep")[i];
                if (caseStep[i].hasOwnProperty("selectStep")) {
                    $(caseStepArrayOld).find("#selectStep option[value='" + caseStep[i].selectStep + "']").attr("selected", true)
                }
                if (caseStep[i].hasOwnProperty("stepContent")) {
                    $(caseStepArrayOld).find("input[name='stepContent']").val(caseStep[i].stepContent)
                }
                //操作方式
                if (caseStep[i].hasOwnProperty("operationStep")) {
                    $(caseStepArrayOld).find("#operationStep option[value='" + caseStep[i].operationStep + "']").attr("selected", true)
                } else {
                    $(caseStepArrayOld).find("#operationStep option[value='" + $(caseStepArrayOld).find("#operationStep").val() + "']").removeAttr("selected")
                }
                //操作内容
                if (caseStep[i].hasOwnProperty("operationContent")) {
                    $(caseStepArrayOld).find("input[name='operationContent']").val(caseStep[i].operationContent)
                } else {
                    $(caseStepArrayOld).find("input[name='operationContent']").val("")
                }


                //断言类型
                $(caseStepArrayOld).find("#assertStep").empty();
                $(caseStepArrayOld).find("#assertStep").append("<option value='0'>断言方式选择</option>")
                for (var j = 0; j < data.data.assertTypes.length; j++) {
                    $(caseStepArrayOld).find("#assertStep").append("<option value=" + data.data.assertTypes[j].id + ">" + data.data.assertTypes[j].name + "</option>")
                }
                if (caseStep[i].hasOwnProperty("assertStep")) {
                    $(caseStepArrayOld).find("#assertStep option[value='" + caseStep[i].assertStep + "']").attr("selected", true)
                }

                //断言类型
                $(caseStepArrayOld).find("#assertType").empty();
                $(caseStepArrayOld).find("#assertType").append("<option value='0'>断言类型选择</option>")
                for (var k = 0; k < data.data.assertTypeNames.length; k++) {
                    $(caseStepArrayOld).find("#assertType").append("<option value=" + data.data.assertTypeNames[k].id + ">" + data.data.assertTypeNames[k].name + "</option>")
                }

                if (caseStep[i].hasOwnProperty("assertType")) {
                    $(caseStepArrayOld).find("#assertType option[value='" + caseStep[i].assertType + "']").attr("selected", true)
                }


                //断言内容
                if (caseStep[i].hasOwnProperty("assertContent")) {
                    $(caseStepArrayOld).find("input[name='assertContent']").val(caseStep[i].assertContent)
                } else {
                    $(caseStepArrayOld).find("input[name='assertContent']").val("")
                }
            }

        },
        error: function (data) {
            toastr.info(data.msg);
        }
    })
}


//添加平台
function showAddModel() {
    $("#addModal").modal({
        backdrop: 'static',
        keyboard: false
    });
}

function openPostType(obj) {
    if ($(obj).val() == 2) {
        $("#bodyTypeParent").css("display", "")
    } else {
        $("#bodyTypeParent").css("display", "none")
    }
}


function addPlatform(obj) {
    var platformName = $("#platformName").val();
    var loginUrl = $("#loginUrl").val();
    var loginParams = $("#loginParams").val();
    var requestType = $("#requestType").val();
    if (platformName == "" || platformName == null) {
        toastr.warning("请添加平台名称");
        return
    }
    if (loginUrl == "" || loginUrl == null) {
        toastr.warning("请添加Url");
        return
    }
    if (loginParams == "" || loginParams == null) {
        toastr.warning("请添加项目登录参数");
        return
    }

    var params = "platformName=" + platformName + "&loginUrl=" + loginUrl + "&loginParams=" + loginParams + "&requestType=" + requestType;
    if (requestType == 2) {
        var bodyType = $("#bodyType").val()
        params += ("&bodyType=" + bodyType)
    }
    $.ajax({
        url: "/ui/addPlatform",
        type: "post",
        data: params,
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                toastr.info(data.msg);
                addPlatformOptions()
            } else {
                toastr.warning(data.msg);
            }
            $("#addModal").modal('hide');

        },
        error: function () {
            toastr.error("网络错误");
            $("#addModal").modal('hide');
        }

    })
}