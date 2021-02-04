"use strict";
window.onload = function (){
    background();
    //$(".login_input").val("admin001")//自动输入默认登录用户名密码
    $(".login_tips").css("display","none")
}
function inputFocus(e){
    $(e).parent().siblings(".login_tips").css("color", "#ddd");
}
function inputBlur(e){
    $(e).parent().siblings(".login_tips").css("color", "#aaa");
}
function inputUp(e){
    var count=$(e).val().length;
    if(count==0){
        $(e).parent().siblings(".login_tips").css("display","block");
    }else{
        $(e).parent().siblings(".login_tips").css("display","none");
    }
}
function background(){
    $(".background_img").css("opacity", "1");
}
function usesrLogin() {
    console.info("login")
    $.ajax({
        url: 'UserServlet',
        type: 'POST',
        async: true,
        timeout: 5000,
        data:{
            method: "userLogin",
            userName: $("#userName").val(),
            userPassword: $("#userPassword").val()
        },
        beforeSend: function (){

        },
        success: function (msg){
            if (msg == "登录成功"){
                $(location).attr('href', 'main.html')
            }else {
                alert(msg)
            }
        },
        error: function (){

        },
        complete: function (){

        }
    });
}
function user(){
    var kouling= prompt("请输入口令:")
    if(kouling==123456)
        usesrRegister()
    else alert("口令错误");
}
function usesrRegister(){
    $.ajax({
        url: 'UserServlet',
        type: 'POST',
        async: true,
        timeout: 5000,
        data:{
            method: "userRegister",
            userName: $("#userName").val(),
            userPassword: $("#userPassword").val()
        },
        beforeSend: function (){

        },
        success: function (msg){
            alert(msg)
        },
        error: function (){

        },
        complete: function (){

        }
    });
}