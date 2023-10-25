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

