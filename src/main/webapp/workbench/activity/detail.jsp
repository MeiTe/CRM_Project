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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
        showRemarkList()
        $("#remarkBody").on("mouseover",".remarkDiv",function(){
            $(this).children("div").children("div").show();
        })
        $("#remarkBody").on("mouseout",".remarkDiv",function(){
            $(this).children("div").children("div").hide();
        })

        //点击保存按钮，触发事件
        $("#saveRemarkBtn").click(function () {
            $("#remark").val("");
            //因为是局部添加，并且需要局部刷新，故需要使用Ajax
            $.ajax({
                url:"workbench/activity/saveRemark.do",
                data:{
                    "noteContent":$.trim($("#remark").val()),
                    "activityId":"${activityDetail.id}"
                },
                type:"post",
                dataType:"json",
                success:function (data) {
                    if (data.success){
                        //表示添加成功，从后台返回的是
                        /*    data
                                    {"success":true/false,"activityRemark":{备注信息}}
                        *
                        * */
                        var html="";
                        html+='<div id="'+data.activityRemark.id+'" class="remarkDiv" style="height: 60px;">';
                        html+='<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                        html+='<div style="position: relative; top: -40px; left: 40px;" >';
                        html+='<h5 id="edit'+data.activityRemark.id+'">'+data.activityRemark.noteContent+'</h5>';
                        html+='<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activityDetail.name}</b> <small style="color: gray;"> '+data.activityRemark.createTime+' 由'+data.activityRemark.createBy+'</small>';
                        html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                        html+='<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+data.activityRemark.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
                        html+='&nbsp;&nbsp;&nbsp;&nbsp;';
                        html+='<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+data.activityRemark.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                        html+='</div>';
                        html+='</div>';
                        html+='</div>';
                        $("#remarkDiv").before(html);
                    }else{
                        alert("添加失败。")
                    }
                }
            })
        })


        //修改备注信息，点击更新按钮进行事件的触发
        $("#updateRemarkBtn").click(function () {
            var id=$("#remarkId").val();
            //因为是局部刷新，所以需要使用到Ajax
            $.ajax({
                url:"workbench/activity/updateRemark.do",
                data:{
                    //前台获取的数据，是备注的ID，和备注最新的修改内容
                    "id":id,
                    "noteContent":$.trim($("#noteContent").val()),
                },
                type:"post",
                dataType:"json",
                success:function (data) {
                    if (data.success){
                        //表示的是，修改成功,从后台返回到前台的除了对和错还有，备注的列表信息   remarkInfo
                        $("#edit"+id).html(data.remarkInfo.noteContent);
                        $("#small"+id).html(data.remarkInfo.editTime+"由"+data.remarkInfo.editBy);
                        //然后再隐藏窗口
                        $("#editRemarkModal").modal("hide");
                    }else{
                        alert("修改失败");
                    }
                }
            })
        })



	});

    //页面加载完毕后，加载这张表
    function showRemarkList() {
        //使用Ajax来显示备注信息
        $.ajax({
            url:"workbench/activity/getRemarkListByAid.do",
            data:{
                //获得活动的ID
                "activityId":"${activityDetail.id}"
            },
            type:"get",
            dataType:"json",
            success:function (data) {
                //从后台传进来的是备注信息的列表
                //进行循环遍历，拼接备注信息
                var html="";
                $.each(data,function (i,n) {
                    html+='<div id="'+n.id+'" class="remarkDiv" style="height: 60px;">';
                    html+='<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                    html+='<div style="position: relative; top: -40px; left: 40px;" >';
                    html+='<h5 id="edit'+n.id+'">'+n.noteContent+'</h5>';
                    html+='<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activityDetail.name}</b> <small style="color: gray;" id="small'+n.id+'"> '+(n.editFlag==0?n.createTime:n.editTime)+' 由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
                    html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                    html+='<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
                    html+='&nbsp;&nbsp;&nbsp;&nbsp;';
                    html+='<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                    html+='</div>';
                    html+='</div>';
                    html+='</div>';
                    //动态生成的元素，必须要套在引号当中。(\''+n.id+'\')     先进性转义，然后保留在引号当中
                })
                //在之前保存备注信息
                $("#remarkDiv").before(html);
            }
        })
    }

    function deleteRemark(id) {
        //根据备注的ID删除，这条备注信息。使用Ajax进行局部刷新
        $.ajax({
            url: "workbench/activity/deleteRemarkInfoById.do",
            data: {
                "remarkId":id
            },
            type: "post",
            dataType: "json",
            success:function (data) {
                //从后台返回一个  true或者是false
                if (data.success){
                    //局部刷新，备注信息列表
                    $("#"+id).remove();
                }else{
                    alert("删除失败！")
                }
            }
        })
    }

    function editRemark(id) {
        //收到获取备注的内容
        var noteContent=$("#edit"+id).html();
        //然后把备注的内容赋值给编辑备注的模态框
        $("#noteContent").val(noteContent);
        //把备注的ID，保存再隐藏域中，因为再修改备注信息的时候，必须要备注的ID
        $("#remarkId").val(id);
        $("#editRemarkModal").modal("show");
    }

</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
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
                    <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="edit-marketActivityOwner" class="col-sm-2 control-label">${activityDetail.owner}<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-marketActivityOwner">
                                    <option>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>
                                </select>
                            </div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">${activityDetail.name}<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-startTime" class="col-sm-2 control-label">${activityDetail.startDate}</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                            </div>
                            <label for="edit-endTime" class="col-sm-2 control-label">${activityDetail.endDate}</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-cost" value="5,000">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-describe">${activityDetail.description}</textarea>
                            </div>
                        </div>

                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activityDetail.name} <small>2020-10-10 ~ 2020-10-20</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activityDetail.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activityDetail.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.createBy}</b><small style="font-size: 10px; color: gray;">${activityDetail.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.editBy}</b><small style="font-size: 10px; color: gray;">${activityDetail.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>${activityDetail.description}</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>




		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>