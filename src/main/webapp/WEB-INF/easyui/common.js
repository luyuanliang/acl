$(function() {
        var $jQuery = jQuery.noConflict();
        $(".datebox :text").attr("readonly","readonly");
        $('#addNewDialog').panel('close');
        $("#updateDialog").panel('close');
        $("#detailDialog").panel('close');
    }
);


function searchRecords1(url){
	searchRecords('#searchForm',url,'#recordGrid');
}

function searchRecords2(searchForm , url){
	searchRecords(searchForm,url,'#recordGrid');
}

function searchRecords3(url,recordGrid){
	searchRecords('#searchForm',url,recordGrid);
}


function getParams(form){
	var t = $(form).serializeArray();
	var params = {};
	for (let val of t) {
		params[val.name] = val.value
	}
	return params;
}

function addReordForm(url){
	var $jQuery =   jQuery.noConflict();  
	var checkPass = $('#addNewForm').form('validate');
	if(!checkPass){
		return;
		}
	$.messager.confirm("新增确认","是否新增记录", function (r) {
	    if (r) {
	    	$('#addNewForm').form('submit', {
	    	    url:url,
	    	    onSubmit: paramAddForm,
	    	    success: function (text) {
	    	    	var ajaxResult = eval('(' + text + ')');
					displayAjaxResult(ajaxResult);
					$('#addNewDialog').panel('close');
				}
	    	});
		 }
    });
}

function executeDefaultParam(p){
	// 把rows 改成pageSize,修改分页参数名称
    p.pageSize=p.rows;
	p.pageNum=p.page;
    return true;
}

function updateOneRecord(url) { 
	
	var $jQuery =   jQuery.noConflict();  
	var checkPass = $('#updateForm').form('validate');
	if(!checkPass){
		return;
	}
	$.messager.confirm("修改确认","是否修改记录", function (r) {
	    if (r) {
	    	$('#updateForm').form('submit', {
	    	    url:url,
	    	    onSubmit: paramUpdateForm,
	    	    success: function (text) {
	    	    	var ajaxResult=eval("("+text+")");
					displayAjaxResult(ajaxResult);
					$('#updateDialog').panel('close');
					$('#recordGrid').datagrid('reload'); 
				},
				error: function () {
					$.messager.alert("操作提示", "修改失败","ERROR");
				}
	    	});
		 }
    });
}

function deleteRecords(url) {
    //var row = $('#recordGrid').datagrid('getSelections');
    var ids = [];
    var deleteIds = "";
    var rows = $('#recordGrid').datagrid('getChecked');
    for(var i=0; i<rows.length; i++){
        ids.push(rows[i].bossMemberRoleMappingId);
        if(i==0){
            deleteIds=rows[i].bossMemberRoleMappingId
        }else {
            deleteIds=deleteIds+","+rows[i].bossMemberRoleMappingId;
        }
    }
    if(ids==""){
        alert("选一个啊！");
    } else {
        var deleteAlert = ids.join('\n');
        deleteAlert = deleteAlert+"\n准备删除以上记录";
        //$.messager.alert("操作提示", deleteAlert,"WARNNING");
        alert(deleteAlert);
        $.messager.confirm("删除确认","是否批量删除记录", function (r) {
            if (r) {
                var $jQuery =   jQuery.noConflict();
                jQuery.ajax({
                    type: 'POST',
                    url: url,
                    data:{
                        deletes :deleteIds
                    },
                    success: function (ajaxResult) {
                        if(ajaxResult.result){
                            delRows();
                        } else {
                            displayAjaxResult(ajaxResult);
                        }
                    },
                    error: function () {
                        $.messager.alert("操作提示", "删除失败","ERROR");
                    }
                });
            }
        });
    }
}


function displayAjaxResult(ajaxResult) {
    var title = ajaxResult.title;
    var msg = ajaxResult.msg;
    var type = ajaxResult.type;
    if(title==null||title==""){
        title = "操作提示";
    }
    if(type!="error") {
    } else if (type!="info") {
    } else {
        type = "warnning";
    }
    $.messager.alert(title , msg , type);
}

function delRows() {
    var recordGirdList = $('#recordGrid');
    var rows = recordGirdList.datagrid("getChecked");
    for(var i =0;i<rows.length;i++) {
        var index = recordGirdList.datagrid('getRowIndex',rows[i]);
        recordGirdList.datagrid('deleteRow',index);
    }
    //$('#recordGrid').datagrid('reload');
}

function init(){
	var $jQuery = jQuery.noConflict();
	$(".datebox :text").attr("readonly","readonly");
	$('#addNewDialog').panel('close');
	$("#updateDialog").panel('close');
	$("#detailDialog").panel('close');
	var themes = [
		  			{value:'default',text:'Default',group:'Base'},
		  			{value:'gray',text:'Gray',group:'Base'},
		  			{value:'metro',text:'Metro',group:'Base'},
		  			{value:'material',text:'Material',group:'Base'},
		  			{value:'bootstrap',text:'Bootstrap',group:'Base'},
		  			{value:'black',text:'Black',group:'Base'},
		  			{value:'metro-blue',text:'Metro Blue',group:'Metro'},
		  			{value:'metro-gray',text:'Metro Gray',group:'Metro'},
		  			{value:'metro-green',text:'Metro Green',group:'Metro'},
		  			{value:'metro-orange',text:'Metro Orange',group:'Metro'},
		  			{value:'metro-red',text:'Metro Red',group:'Metro'},
		  			{value:'ui-cupertino',text:'Cupertino',group:'UI'},
		  			{value:'ui-dark-hive',text:'Dark Hive',group:'UI'},
		  			{value:'ui-pepper-grinder',text:'Pepper Grinder',group:'UI'},
		  			{value:'ui-sunny',text:'Sunny',group:'UI'}
		  		];
		$('#cb-theme').combobox({
			groupField:'group',
			data: themes,
			editable:false,
			panelHeight:'auto',
			onChange:onChangeTheme,
			onLoadSuccess:function(){
				$(this).combobox('setValue', 'material');
			}
		});		
}

function onChangeTheme(theme){
	var link = $('#css');
	link.attr('href', 'http://www.jeasyui.com/easyui/themes/'+theme+'/easyui.css');
}

function onLoadSuccess(data){
    $('#recordGrid').datagrid("getRows");
}


function deleteOneRecord(row,url) {
    $.messager.confirm("删除确认","是否删除记录", function (r) {
        if (r) {
            var $jQuery =   jQuery.noConflict();
            jQuery.ajax({
                type: 'POST',
                url: url,
                data:{
                    deletes :row
                },
                success: function (text) {
                	var ajaxResult=eval("("+text+")");
                    if(ajaxResult.result){
                        $.messager.alert("操作提示", ajaxResult.msg,ajaxResult.type);
                        var recordGridList = $('#recordGrid');
                        var rows = recordGridList.datagrid("getRows");
                        for(var i =0;i<rows.length;i++) {
                            if(checkDeleteRow(row,rows,i)){
                                var index = recordGridList.datagrid('getRowIndex',rows[i]);
                                recordGridList.datagrid('deleteRow',index);
                                break;
                            }
                        }
                    } else {
                        $.messager.alert( "操作提示", ajaxResult.msg , ajaxResult.type);
                    }

                },
                error: function () {
                    $.messager.alert("操作提示", "删除失败","ERROR");
                }
            });
        }
    });
}

function deleteOneRecord(listName,row,url) {
    $.messager.confirm("删除确认","是否删除记录", function (r) {
        if (r) {
            var $jQuery =   jQuery.noConflict();
            var data = {};
            data[listName] = row;
            jQuery.ajax({
                type: 'POST',
                url: url,
                data:data,
                success: function (text) {
                    var ajaxResult=eval("("+text+")");
                    if(ajaxResult.result){
                        $.messager.alert("操作提示", ajaxResult.msg,ajaxResult.type);
                        var recordGridList = $('#recordGrid');
                        var rows = recordGridList.datagrid("getRows");
                        for(var i =0;i<rows.length;i++) {
                            if(checkDeleteRow(row,rows,i)){
                                var index = recordGridList.datagrid('getRowIndex',rows[i]);
                                recordGridList.datagrid('deleteRow',index);
                                break;
                            }
                        }
                    } else {
                        $.messager.alert( "操作提示", ajaxResult.msg , ajaxResult.type);
                    }

                },
                error: function () {
                    $.messager.alert("操作提示", "删除失败","ERROR");
                }
            });
        }
    });
}


$(function () {
    $(".panel-noscroll")
        .css({
            top: 0,
            right: 0,
            bottom: 0,
            left: 0
        });

    $(".panel.datagrid.easyui-fluid").css('width', '100%')
        .find('.panel-header').css('width', '100%').end()
        .find('.datagrid-wrap.panel-body').css('width', '100%')
        .find('.datagrid-view').css('width', '100%');
})



