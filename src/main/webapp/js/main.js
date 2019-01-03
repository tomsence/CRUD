 $(function (data) {
    $("#tb1").datagrid({
        url:"http://localhost:8080/pm/user/reload",
        method: 'get',  
        toolbar: '#tb', 
        fitColumns:true,
        rownumbers:true, 
        striped:true,
        columns: [[  
            { field: 'user_name', title: '用户名', width: 100 },  
            { field: 'user_passwd', title: '用户密码', width: 100 },  
            { field: 'email', title: '电子邮箱', width: 100},  
            { field: 'telephone', title: '电话', width: 100 },  
            { field: 'status', title: '状态', width: 100 },  
            { field: 'remark', title: '备注', width: 100 }
        ]],  
        singleSelect:true,
        pagination: true,  
        pageSize:10,  
        pageList: [10, 15, 20, 25],
        data:data.row
    })
})

//弹窗-“新增用户”
    function newEqument() {  
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增用户');  
        $('#fm').form('clear');  
    }  

//弹窗-“上传附件”
    function uploadEqument(){
        $('#upload').dialog('open').dialog('center').dialog('setTitle','上传附件');
        $('#upload_form').form('clear');
    }

//提交保存附件
/** 
    function uploadUser(){
        var file=$('#contractFile').textbox('getValue');
        var formData = new FormData;
        formData.append('file',file);
        $.ajax({
            url:"http://localhost:8080/pm/user/upload",
            type:"post",
            dataType:'json',
            data:formData,
            cache:false,
            contentType:false,
            processData:false,
            success:function(data){
                if(data.status == true){
                    $('#upload').dialog('close');
                    reload(data);
                }else{
                    alert(data.message);
                }
            }
        })
    }
**/
    function uploadUser(){
        $('#upload_form').form('submit',{
            url:"http://localhost:8080/pm/user/upload",
            onsubmit:function(){

            },
            success:function(data){
                alert("上传成功!");
                $('#upload').dialog('close');
                reload(data); 
                /*
                if(data.status == true){
                    $('#upload').dialog('close');
                    alert(data.message);
                    reload(data);  
                }else{
                    alert(data.message);
                } 
                */
            }
        })
    }

//弹窗-“修改用户”
    function editEqument(){
         
       var row = $('#tb1').datagrid('getSelected');
       //console.log(row.userName);
       if(row){
           $('#mod').dialog('open').dialog('center').dialog('setTitle', '修改用户');  
           $('#userName_mod').textbox('setValue',row.userName);
           $('#userName_mod').textbox('textbox').attr('readonly',true);  //设置输入框为禁用
           $('#userPasswd_mod').textbox('setValue','');
           $('#userPasswd1_mod').textbox('setValue','');
           $('#email_mod').textbox('setValue',row.email);
           $('#telephone_mod').textbox('setValue',row.telephone);
           $('#remark_mod').textbox('setValue',row.remark);
       }else{
           $.messager.alert("提示","请选中要编辑的行!");
       }
    }

//删除-所选用户
    function destroyEqument(){
        var row = $('#tb1').datagrid('getSelected');
        if(row){
            var rowIndex = $('#tb1').datagrid('getRowIndex',row);
            $('#tb1').datagrid('deleteRow',rowIndex);
            delUser(row.userName);
        }else{
            $.messager.alert("提示","请选择删除的行!");
        }
    }

//删除-所选用户（后台）
    function delUser(username){
        var data_snd={'userName':username};
        console.log(data_snd);
        $.ajax({
            url:"http://localhost:8080/pm/user/del",
            type:"get",
            dataType:"json",
            crossDomain:true,
            headers:{
              "Page-Code": "",
              "Content-Type": "application/json"
            },
            xhrFields: {
              "withCredentials": true
              },
            //传送请求数据，JSON格式
			data:{userName:username},
            success:function(data){
                alert(data.message);
                reload(data);
            }
          }) 
    }

//刷新-所有用户
    function reloadEqument(){
        reload();
    }

//刷新-“所有用户”
    function reload(){
      $.ajax({
        url:"http://localhost:8080/pm/user/reload",
        type:"get",
        dataType:"json",
        crossDomain:true,
        headers:{
          "Page-Code": "",
          "Content-Type": "application/json"
        },
        xhrFields: {
          "withCredentials": true
          },
        //传送请求数据，JSON格式
        //data:JSON.stringify(data_snd),
        data:"page=1&rows=10",
        success:function(data){
            if(data.status == true){
                //console.log("2222222");
                console.log(data.rows);
                $("#tb1").datagrid({
                    data:data.rows
                });
            }else{
                alert(data.message);
            }
        }
      })  
    }

//修改-“修改用户信息”
    function editUser(){
        var formName = encodeURI($("#userName_mod").val());
        var formPasswd = encodeURI($("#userPasswd_mod").val());
        var formPasswd1 = encodeURI($("#userPasswd1_mod").val());
        var formEmail = encodeURI($("#email_mod").val());
        var formTel = encodeURI($("#telephone_mod").val());
        var formBeiz = encodeURI($("#remark_mod").val());
        if( formPasswd != formPasswd1)
        {
            alert("两次输出密码不符!");
        }
        var data_snd={'id':0,'userName':formName,'userPasswd':formPasswd,'email':formEmail,'telephone':formTel,'status':'0','remark':formBeiz};
        console.log(data_snd);
        //开始发送数据
		$.ajax(
		{
			//请求登录页
			url:"http://localhost:8080/pm/user/mod",
			//提交方式
			type:"post",
			dataType:"json",
            crossDomain:true,
            headers: {
                "Page-Code": "",
                "Content-Type": "application/json"
				},
			xhrFields: {
                "withCredentials": true
            	},
			//传送请求数据，JSON格式
			data:JSON.stringify(data_snd),
			success:function(data){//登录成功后返回的数据
                //根据返回值进行状态判断
                console.log(data.status);  
                //var data=JSON.stringify(data);
                if(data.status == true){
                    $('#mod').dialog('close');
                    reload(data);
                    alert(data.message);
                    //console.log(data.message);
                }else{
                    console.log(data.message);
                    console.log("22222");
                    alert(data.message);
                }
            }
       }) 
    }

//保存-“新增用户”
    function saveUser(){
        var formName = encodeURI($("#userName").val());
        var formPasswd = encodeURI($("#userPasswd").val());
        var formPasswd1 = encodeURI($("#userPasswd1").val());
        var formEmail = encodeURI($("#Email").val());
        var formTel = encodeURI($("#Telephone").val());
        var formBeiz = encodeURI($("#beiz").val());
        if( formPasswd != formPasswd1)
        {
            alert("两次输出密码不符!");
        }
        var data_snd={'id':0,'userName':formName,'userPasswd':formPasswd,'email':formEmail,'telephone':formTel,'status':'0','remark':formBeiz};
        console.log(data_snd);
        //开始发送数据
		$.ajax(
		{
			//请求登录页
			url:"http://localhost:8080/pm/user/add",
			//提交方式
			type:"post",
			dataType:"json",
            crossDomain:true,
            headers: {
                "Page-Code": "",
                "Content-Type": "application/json"
				},
			xhrFields: {
                "withCredentials": true
            	},
			//传送请求数据，JSON格式
			data:JSON.stringify(data_snd),
			success:function(data){//登录成功后返回的数据
                //根据返回值进行状态判断
                console.log(data);
                $("#dlg").dialog('close');
                //var data=JSON.stringify(data);
                if(data.status == true){
                    alert(data.message);
                    //console.log(data.message);
                }else{
                    console.log(data.message);
                    alert(data.message);
                }
            }
       })
    }

//登出
    function logout(){
        var data_snd={'userName':localStorage.userName}
        //开始发送数据
		$.ajax(
        {
            //请求登录页
            url:"http://localhost:8080/pm/logout",
            //提交方式
            type:"post",
            dataType:"json",
            crossDomain:true,
            headers: {
                "Page-Code": "",
                //"Content-Type": "application/json"
                "Content-Type":"application/x-www-form-urlencoded"
                },
            xhrFields: {
                "withCredentials": true
                },
            //传送请求数据，JSON格式;如转换成json,则后端pojo对象识别不了.
            //data:JSON.stringify(data_snd),
            data:{'userName':localStorage.userName},
            success:function(data){//登录成功后返回的数据
                //根据返回值进行状态判断
                console.log("eeeeeeeeeeeee");
                console.log(data);
                if(data.status == true){
                    window.location.href("http://localhost:8080/pm/index.html");
                }else{
                    console.log(data.message);
                    alert(data.message);
               }
            }
        })
    }