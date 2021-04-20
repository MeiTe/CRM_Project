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
	<script>
		$(function () {

			if (window.top!=window){
				window.top.location=window.location;
			}

			//让用户的账户名自动获取焦点，然后每次刷新的时候测试其值为空，不空浏览器的功能不一样，有的话自动保留上一次残留下来的取值
			$("#loginAct").focus();
			$("#loginAct").val("")
			//点击登录按钮进行登录的验证
			$("#submitBtn").click(function () {
				login();
			})
			//敲击回车键，进行表单的tijiao
			$(window).keydown(function (event) {
				//  alert(event.keyCode);   //由此测得，回车键的键码是13
				if (event.keyCode==13){
					login();
				}
			})
		})

		//方法写在函数的外面
		function login() {
			//首先要验证用户输入的账号密码，不可以为空。需要将文本中的左右空格去掉，使用$.trim(文本)
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			if (loginAct=="" || loginPwd==""){
				$("#msg").html("用户的账号或者密码不可以为空！！！");
				//如果账号和密码没有填的话，那么下面的ajax请求就不可以继续执行
				return false;
			}
			//否则的话，就是有账号和密码。进行账号和密码的验证、
			$.ajax({
				url:"settings/user/login.do",    /*这里前面没有没有斜杠*/
				data:{
					//这里面填写的是给后台传的数据，这里把用户的账号和密码传给后台。前面是名称后面是传的数据
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					/*
					* 对于传送的数据只有两种结果，一个是成功，一个是失败。如果出错的话，返回回来哪里错了
					* {"success":false/true,"msg":哪错了}
					* */
					if (data.success){
						//登录成功，进行主页面的跳转
						window.location.href="workbench/index.jsp";
					}else {
						//失败的话，进行信息的提示
						$("#msg").html(data.msg);
					}
				}
			})

		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;上海海事大学</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red;"></span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>