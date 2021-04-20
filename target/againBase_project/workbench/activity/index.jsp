<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" >

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//引入时间控件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		//点击创建按钮，弹出模态框
		$("#addBtn").click(function () {
			//在模态框弹出来之前，需要查询用户表里面的用户名信息，并且进行显示。使用Ajax
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success : function (data) {
					var html="<option></option>";
					//进行遍历,   i表示的是数量，取值为1，2，3···；n表示的是每一条用户的信息
					$.each(data,function (i,n) {
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					//然后把查询出来的内容放到模态框里面的下拉框进行显示
					$("#create-marketActivityOwner").html(html);
					//获得当前用户的ID
					var id="${user.id}";
					//默认让下拉框选显示当前用户
					$("#create-marketActivityOwner").val(id);
					// data-toggle="modal" data-target="#createActivityModal"
					$("#createActivityModal").modal("show");
				}
			});
		});

		//添加模态框的内容，添加活动信息，使用Ajax进行局部刷新
		//点击保存按钮触发函数
		$("#saveBtn").click(function () {
			//使用Ajax
			$.ajax({
				url: "workbench/activity/save.do",
				type: "post",
				data:{
					//这里是传到后台去的数据,前面和数据库的字段进行对应
					"owner":$.trim($("#create-marketActivityOwner").val()),
					"name":$.trim($("#create-marketActivityName").val()),
					"startDate":$.trim($("#create-startTime").val()),
					"endDate":$.trim($("#create-endTime").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-describe").val()),

				},
				dataType: "json",
				success:function (data) {
					//通过返回true和false,来进行反馈
					if (data.success){
						//$("#addActivityForm")[0].reset();
						//添加成功，隐藏这个模态框
						$("#createActivityModal").modal("hide");
						/*
							这里是添加操作，操作之后，应该回到第一页。但是用户设置的显示多少页保持不变。

						* */
						pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
					}else {
						alert("添加失败！！")
					}
				}
			})
		})

		//当页面加载完成之后，动态刷新列表显示的内容。传入当前页面和每一页显示的条数
		pageList(1,5);
		//为查询按钮绑定事件
		$("#searchBtn").click(function () {
			//点击查询按钮的时候，把输入框里面的内容放在，隐藏域里面进行保存
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startTime").val($.trim($("#search-startTime").val()));
			$("#hidden-endTime").val($.trim($("#search-endTime").val()));
			//显示第一页，但是用户设置的显示条数不变
			pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));

		});

		//点击第一个复选框，实现全选
		$("#checkFirst").click(function () {
			//选中当前页面所有的复选框
			$("input[name=fuXuanKuang]").prop("checked",this.checked);
		})
		/*
		动态生成：通过JS拼接而成的
		动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
		动态生成的元素，我们要用on方法的形式来触发事件
		语法：
		$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)
		* */
		$("#activityBody").on("click",$("input[name=fuXuanKuang]"),function () {
			$("#checkFirst").prop("checked",$("input[name=fuXuanKuang]").length==$("input[name=fuXuanKuang]:checked").length);
		})

		//对选中的条目进行删除
		$("#deleteBtn").click(function () {
			var $fuXuanKuang=$("input[name=fuXuanKuang]:checked");
			if ($fuXuanKuang.length==0){
				alert("请选中删除的条目。");
			}else {
				if (confirm("你确定要删除吗？")){
					//进行删除操作，这里可以删除一条或者是多条。
					//因为传到后台的是   id=?&id=?&id=?&id=?&id=?```的格式，所以需要拼接字符串
					var param="";
					for (var i =0;i<$fuXuanKuang.length;i++){
						param+="id="+$($fuXuanKuang[i]).val();//获取选中的复选框的ID值
						//除了最后一个不加&，其他的都需要添加&符号，拼接字符串
						if (i<$fuXuanKuang.length-1){
							param+= "&";
						}
					}
					//删除的时候，局部刷新列表，需要使用Ajax
					$.ajax({
						url:"workbench/activity/deleteActivity.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function (data) {
							//让其返回删除true和false的形式
							if (data.success){
								//表示删除成功，刷新列表。显示第一页，但是用户设置的显示条数不变
								pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
							}else{
								alert("删除失败！")
							}
						}
					})
				}
			}
		})
		//修改活动的信息
		$("#editBtn").click(function () {
			$("#editActivityModal").modal("hide");
			var $fuXuanKuang=$("input[name=fuXuanKuang]:checked");
			if ($fuXuanKuang.length==0){
				alert("请选中需要修改的活动信息！");
			}else if ($fuXuanKuang.length>1){
				alert("每次只可以修改一条活动信息！");
			}else {
				//表示选中的是一条,使用Ajax获取，根据ID的值，获取相关的活动信息
				var id=$fuXuanKuang.val();
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
						//从后台返回的相关信息
						/*
						* data
						* 		用户列表
						* 		市场活动对象
						* 		{"uList":[{用户1},{用户2},{用户3}],"activityInfo":{市场活动}}
						* */
						var html="<option></option>";
						$.each(data.uList,function (i,n) {
							//对所有者拼接字符串
							html+="<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-owner").html(html);
						//处理单条的activity
						$("#edit-id").val(data.activityInfo.id);
						$("#edit-owner").val(data.activityInfo.owner);
						$("#edit-name").val(data.activityInfo.name);
						$("#edit-startDate").val(data.activityInfo.startDate);
						$("#edit-endDate").val(data.activityInfo.endDate);
						$("#edit-describe").val(data.activityInfo.description);
						$("#edit-cost").val(data.activityInfo.cost);
						//然后显示模态框
						$("#editActivityModal").modal("show");
					}

				})

			}
		})
		//修改活动信息的操作
		$("#updateBtn").click(function () {
			//类似于添加操作，从页面中获取值,需要使用Ajax
			$.ajax({
				url:"workbench/activity/update.do",
				type:"post",
				data:{
					//从页面中获取数据，然后传到后台
					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"description":$.trim($("#edit-describe").val()),
					"cost":$.trim($("#edit-cost").val()),
				},
				dataType:"json",
				success:function (data) {
					//想要从后台传回，修改成功还是修改失败
					if (data.success){
						//修改成功之后，显示当前页。用户设置的显示多少页，页面数不变。
						pageList($("#activityPage").bs_pagination('getOption','currentPage'),
								$("#activityPage").bs_pagination('getOption','rowsPerPage'));
						$("#editActivityModal").modal("hide");
					}else{
						alert("修改失败！")
					}
				}
			})
		});
	});

	function pageList(pageNo,pageSize) {
		//每次点击和分页相关的按钮的时候，从隐藏域中获取搜索的值
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startTime").val($.trim($("#hidden-startTime").val()));
		$("#search-endTime").val($.trim($("#hidden-endTime").val()));

		//使用Ajax进行局部的刷新
		$.ajax({
			url:"workbench/activity/pageList.do",
			data: {
				//这里是前台传到后台去的
				//获取显示的页面数和条数
				"pageNo":pageNo,
				"pageSize":pageSize,
				//获取用户查询的时候的信息
				"owner":$.trim($("#search-owner").val()),
				"name":$.trim($("#search-name").val()),
				"startDate":$.trim($("#search-startTime").val()),
				"endDate":$.trim($("#search-endTime").val())
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				/*
				* 前台想要从后台获得什么样的信息。
				* 1、因为是活动信息，所以我们要获取活动的列表。[{活动1名称，活动1所有者，开始时间，结束时间，···},{活动2名称，活动2所有者，开始时间，结束时间，···},{活动3名称，活动3所有者，开始时间，结束时间，···},````]
				* 2、分页插件需要用到，活动列表总共的条数
				* 故，我们可以从后台获取
				* {“total”：100,"dataList":[{活动1名称，活动1所有者，开始时间，结束时间，···},{活动2名称，活动2所有者，开始时间，结束时间，···},{活动3名称，活动3所有者，开始时间，结束时间，···},````]}
				* */
				var html="";
				//对返回的数据中的列表进行遍历
				$.each(data.dataList,function (i,n) {  //i表示的是第几条活动信息，n表示的是这条活动信息的详情
					html+='<tr class="active">';
					html+='<td><input type="checkbox" name="fuXuanKuang" value="'+n.id+'" /></td>';
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html+='<td>'+n.owner+'</td>';
					html+='<td>'+n.startDate+'</td>';
					html+='<td>'+n.endDate+'</td>';
					html+='</tr>';
				})
				//然后存放在body里面
				$("#activityBody").html(html);

				//分页的总页数。total来自VO中的total，保持一直，进行获取，因为通过JSON封装到了，前台，因此名称不变。
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//数据处理完毕之后，进行分页内容的显示
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//这个回调函数是在点击，分页的时候出发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		});
		//每一次刷新列表之后，取消全选
		$("#checkFirst").prop("checked",false);

	}
	
</script>
</head>
<body>

	<%--通过隐藏input来保存，搜索条件--%>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startTime">
	<input type="hidden" id="hidden-endTime">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="addActivityForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">

								</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkFirst"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>

			<%--这里显示的是分页信息--%>
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">
				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>