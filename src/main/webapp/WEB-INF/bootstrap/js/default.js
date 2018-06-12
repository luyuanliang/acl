function showAlert2(type, message) {
    showAlert(null, type, message);
}

function showAlert(title, type, message) {
    if (title == null || title == "") {
        if (type == "error") {
            title = "错误！";
        } else if (type == "warning") {
            title = "警告！";
        } else if (type == "success") {
            title = "成功！";
        } else {
            title = "操作提示";
        }
    }
    if (message == null || message == "") {
        if (type == "error") {
            message = "操作失败,系统异常！";
        } else if (type == "warning") {
            message = "操作警告！";
        } else if (type == "success") {
            message = "操作成功！";
        }
    }
    if (type == "error") {
        type = "type-danger";
    } else if (type == "warning") {
        type = "type-warning";
    } else {
        type = "type-success";
    }
    BootstrapDialog.show({
        cssClass: 'dialog-width',
        type: type,
        title: title,
        message: message,
        draggable: true,
        closeByBackdrop: true,
        onshown: function (dialogRef) {
            setTimeout(function () {
                dialogRef.close();
            }, 3500);
        }
    });
}

function rowStyle(row, index) {
    //var classes = ['active', 'success', 'info', 'warning', 'danger'];
    //var classes = ['active', 'success', 'info', 'warning', 'danger'];
	var classes = ['active', 'success'];
    return {
        classes: classes[index % 2]
    };
}

function showTime(componentName) {
    $(componentName).datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });
}

function showDate(componentName) {
    $(componentName).datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        showMeridian: true,
        pickerPosition: "bottom-left",
        language: 'zh-CN',//中文，需要引用zh-CN.js包
        startView: 2,//月视图
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
    });
}

function transArray2JsonMap(array) {
    var params = {};
    //noinspection JSAnnotator
    for (let val of array) {
        params[val.name] = val.value
    }
    return params;
}

function searchList(needCountAgain) {
    if (needCountAgain == null) {
        needCountAgain = true;
    }
    var params = transArray2JsonMap($("#search_form").serializeArray());

    var options = $('#table').bootstrapTable('getOptions');
    params['pageSize'] = options.pageSize;
    params['sortOrder'] = options.sortOrder;
    params['sortName'] = options.sortName;
    if (needCountAgain) {
        params['pageNumber'] = 1;
    } else {
        params['pageNumber'] = options.pageNumber;
        params['totalRows'] = options.totalRows;
    }

    $.ajax({
        type: "post",
        url: getSearchListUrl(),      // 这里是提交到什么地方的url
        data: params,            // 这里把表单里面的数据放在这里传到后台
        dataType: "json",
        success: function (res) {
            // 调用回调函数
            $("#table").bootstrapTable('load', res);
        },
        error: function () {
            showDialog2("error", null);
        }
    });
}

function init() {

    $('#updateDialog').hide();
    $('#detailDialog').hide();
    $('#addDialog').hide();

    BootstrapDialog.defaultOptions = {
        nl2br: false,
    };

    $('#table').on('page-change.bs.table', function (number, size) {
        searchList(false);
    })
    searchList(true);
    // $('#toolbar').find('select').change(function () {
    //     var  exportWay= $('#exportWay').val();
    //     alert(exportWay);
    //     $("#table33").bootstrapTable({
    //         exportDataType: exportWay
    //     });
    // });
}

function addNewRecord(url) {
    var form = $('#OPEN_ADD_FORM_ID_' + (OPEN_ADD_ID-1));
    var validator =  form.data('bootstrapValidator');
    form.bootstrapValidator(validateAddForm());
    if(validator.isValid()){
        var params = transArray2JsonMap(form.serializeArray());
        $.ajax({
            type: "post",
            url: url,      // 这里是提交到什么地方的url
            data: getInsertRecordParam(params),            // 这里把表单里面的数据放在这里传到后台
            dataType: "json",
            success: function (res) {
                // 调用回调函数
                if (res.result) {
                    addDialog.close();
                    showAlert2("success", res.message);
                    searchList(false);
                } else {
                    showAlert2("error", res.message);
                }
            },
            error: function () {
                showAlert2("error", null);
            }
        });
    }
}

function addDialogOpen() {
    addDialog = BootstrapDialog.show({
        closeByBackdrop: false,
        type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
        closable: true, // <-- Default value is false
        draggable: true, // <-- Default value is false
        title: '请认真添加',
        message: $('<div>' + $('#addDialog').html() + '</div>').find('form').attr('id', 'OPEN_ADD_FORM_ID_' + OPEN_ADD_ID++).end().html(),
        onshow: function (dialogRef) {
        },
        onshown: function (dialogRef) {
//            $('.add_form').bootstrapValidator(validateAddForm()).on('success.form.bv', function (e) {
//                e.preventDefault();
//            });
        	 $('.add_form').bootstrapValidator(validateAddForm());
        },
        onhidden: function (dialogRef) {
        },
    });
}

function IdFormatter(value, row, index) {
    var options = $('#table').bootstrapTable('getOptions');
    return options.pageSize * (options.pageNumber - 1) + index + 1;
}

function updateOneRecord(url) {
    var form = $('#OPEN_UPDATE_FORM_ID_' + (OPEN_FORM_ID-1));
    var validator =  form.data('bootstrapValidator');
    form.bootstrapValidator(validateUpdateForm());
    if(validator.isValid()){
        var params = transArray2JsonMap(form.serializeArray());
        $.ajax({
            type: "post",
            url: url,      // 这里是提交到什么地方的url
            data: getUpdateRecordParam(params),            // 这里把表单里面的数据放在这里传到后台
            dataType: "json",
            success: function (res) {
                // 调用回调函数
                if (res.result) {
                    showAlert2("success", res.message);
                    searchList(false);
                    updateDialog.close();
                } else {
                    showAlert2("error", res.message);
                }
            },
            error: function () {
                showAlert2("error", null);
            }
        });
    }
}

function selectDetailRecord(paramName, primaryId, url) {
    var params = {};
    params[paramName] = primaryId;
    $.ajax({
        type: "post",
        url: url,      // 这里是提交到什么地方的url
        data: params,            // 这里把表单里面的数据放在这里传到后台
        dataType: "json",
        success: function (ajaxResult) {
            // load form
            if (ajaxResult.result) {
                // 调用回调函数
                var detailDialog = BootstrapDialog.show({
                    title: '查询记录详情',
                    message: $('#detailDialog').html(),
                    closeByBackdrop: true,
                    type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
                    closable: true, // <-- Default value is false
                    draggable: true, // <-- Default value is false
                    buttons: [{
                        label: '关闭',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }]
                });
                detailDialog.onShown(function () {
                    loadDetailForm(detailDialog,ajaxResult);
                });
            } else {
                showAlert2("error", res.message);
            }
        },
        error: function () {
            showAlert2("error", null);
        }
    });
}

function updateRecordOpen(paramName, primaryId, url) {
    var params = {};
    params[paramName] = primaryId;
    $.ajax({
        type: "post",
        url: url,      // 这里是提交到什么地方的url
        data: params,            // 这里把表单里面的数据放在这里传到后台
        dataType: "json",
        success: function (ajaxResult) {
            // load form
            if (ajaxResult.result) {
                // 调用回调函数
                updateDialog = BootstrapDialog.show({
                    title: '警告,请小心修改',
                    message: $('<div>' + $('#updateDialog').html() + '</div>').find('form').attr('id', 'OPEN_UPDATE_FORM_ID_' + OPEN_FORM_ID++).end().html(),
                    type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
                    closable: true, // <-- Default value is false
                    draggable: true, // <-- Default value is false
                    onshown: function (dialogRef) {
                    	 $('.update_form').bootstrapValidator(validateUpdateForm());
//                        $('.update_form').bootstrapValidator(validateUpdateForm()).on('success.form.bv', function (e) {
//                        	
//                            e.preventDefault();
//                            // result will be true if button was click, while it will be false if users close the dialog directly.
//                        });
                        var form = $('#OPEN_UPDATE_FORM_ID_' + (OPEN_FORM_ID-1));
                        loadUpdateForm(form,ajaxResult);
                    },
                });
            } else {
                showAlert2("error", res.message);
            }
        },
        error: function () {
            showAlert2("error", null);
        }
    });
}



/**
 * 批量删除记录
 * */
function deleteRecords(field,url,paramName) {
    var ids = $.map($('#table').bootstrapTable('getSelections'), function (row) {
        return row[field];
    });
    if (ids == null || ids.length == 0) {
        showAlert2("error", "请选择要删除的记录");
        return;
    }
    BootstrapDialog.confirm({
        title: '警告',
        message: '确认批量删除记录?',
        type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
        closable: true, // <-- Default value is false
        draggable: true, // <-- Default value is false
        btnCancelLabel: '取消', // <-- Default value is 'Cancel',
        btnOKLabel: '删除', // <-- Default value is 'OK',
        btnOKClass: 'btn-warning', // <-- If you didn't specify it, dialog type will be used,
        callback: function (result) {
            // result will be true if button was click, while it will be false if users close the dialog directly.
            if (result) {
                var params = {};
                var idList = ids.join(',');
                params[paramName] = idList;
                $.ajax({
                    type: "post",
                    url: url,      // 这里是提交到什么地方的url
                    data: params,            // 这里把表单里面的数据放在这里传到后台
                    dataType: "json",
                    success: function (res) {
                        // 调用回调函数
                        $('#table').bootstrapTable('remove', {
                            field: field,
                            values: ids
                        });
                        //searchList();
                    },
                    error: function () {
                        showAlert2("error", null);
                    }
                });
            }
        }
    });
}

/**
 * 删除一条记录
 * */
function deleteOneRecord(paramName, primaryId, url) {
    BootstrapDialog.confirm({
        title: '警告',
        message: '确认删除记录?',
        type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
        closable: true, // <-- Default value is false
        draggable: true, // <-- Default value is false
        btnCancelLabel: '取消', // <-- Default value is 'Cancel',
        btnOKLabel: '删除', // <-- Default value is 'OK',
        btnOKClass: 'btn-warning', // <-- If you didn't specify it, dialog type will be used,
        callback: function (result) {
            // result will be true if button was click, while it will be false if users close the dialog directly.
            if (result) {
                var params = {};
                params[paramName] = primaryId;
                $.ajax({
                    type: "post",
                    url: url,      // 这里是提交到什么地方的url
                    data: params,            // 这里把表单里面的数据放在这里传到后台
                    dataType: "json",
                    success: function (res) {
                        // 调用回调函数
                        $('#table').bootstrapTable('remove', {
                            field: paramName,
                            values: [primaryId],
                        });
                    },
                    error: function () {
                        showAlert2("error", null);
                    }
                });
            }
        }
    });
}
