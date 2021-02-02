"use strict";
window.onload = function (){
    load();
    selectUp();
}
var Page=1;
var p=1;
function closeDialog(){
    //隐藏对话框
    $(".el-talk__wrapper").hide()
    $(".el-dialog__wrapper").hide()
    $(".v-modal").hide()
    //清空对话框
    $("#sno").val(''),
        $("#sname").val(''),
        $("#sdatebirth").val(''),
        $("#ssex").val(''),
        $("#snativeplace").val(''),
        $("#shouseaddress").val(''),
        $("#snation").val(''),
    document.getElementById("no").innerHTML='',
    document.getElementById("Chinese").value='',
    document.getElementById("math").value='',
    document.getElementById("English").value=''
}
function selectUp(){
    var i //下拉框的箭头
    var div //下拉框的input的父元素
    var input = $('#search') //下拉框的input
    var dropdown = $(".el-select-dropdown") //下拉框内容的body
    var s = $(".el-select") //下拉框的body
    $("#select-dropdown").click(function () {
        var t = $(this);
        i = t.children('.el-input__suffix').children('.el-input__suffix-inner').children('.el-icon-arrow-up')
        div = i.parent().parent().parent(".el-input")
        dropdown.css({
            "min-width": parseInt(s.css("width")) + 'px',
            "top": parseInt(s.css("top")) + s.offset().top + s.height() + 'px',
            "left": parseInt(s.css("left")) + s.offset().left + 'px'
        })// display: none; min-width: 90px; transform-origin: center top 0px; z-index: 2123; position: absolute; top: 65px; left: 28px;"
        // 根据el-select的长宽以及坐标设置下拉框出现的位置
        if (i.hasClass('is-reverse') == false) {
            i.addClass('is-reverse')
            div.addClass('is-focus')
            dropdown.slideDown("slow");
        } else {
            i.removeClass('is-reverse')
            div.removeClass('is-focus')
            dropdown.slideUp("slow");
        }
    });
    $(".el-select-dropdown__list li").click(function () {
        var index = $(this).index()
        $(this).siblings('li').removeClass('selected');
        $(this).addClass('selected')
        $(".el-input--suffix").children(".el-input__inner").val($(this).text())
        i.removeClass('is-reverse')
        div.removeClass('is-focus')
        dropdown.slideUp("slow")
        input.attr('key', $(this).attr('key'))
        if (input.attr('key') == 'sdatebirth') {
            $("#search_text").attr("type", "date")
        }
    })
    $(".el-select-dropdown__list li").hover(function (){
        $(this).siblings('li').removeClass('hover')
        $(this).addClass('hover')

    })
}
function StuGrade(redSno){
    if($(".el-talk__wrapper").css("display")=="none"){
        $(".el-dialog__title").text("学生成绩，可通过直接输入修改")
        $("#dlog_yes").attr("onclick", "StuGrade("+redSno+")")
        $(".el-talk__wrapper").show()
        $(".v-modal").show()
        var sno=redSno;
        document.getElementById("no").innerHTML= sno;
        searResult(redSno);
        return
    }
    $.ajax({
        url: 'StudentServlet', //要请求的url地址
        type: 'POST',
        async: true, //是否使用异步请求的方式
        timeout: 5000, //请求超时的时间，以毫秒计
        data:{
            method: 'StuGrade',
            sno: redSno,
            chinese: document.getElementById("Chinese").value,
            math: document.getElementById("math").value,
            english: document.getElementById("English").value,
        },
        beforeSend: function (){

        },
        success: function (msg){
            if(msg=="修改成功"){
                closeDialog()
            }
            alert(msg)
            clarJsonTable()
            load()
        },
        error: function (){
            alert("修改失败")
        },
        complete: function (){

        }
    });
}
function addStudent(){
    if ($(".el-dialog__wrapper").css("display") == "none"){
        $(".el-dialog__title").text("添加学生")
        $("#dialog_yes").attr("onclick", "addStudent()")
        $(".el-dialog__wrapper").show()
        $(".v-modal").show()
        return
    }
    $.ajax({
        url: 'StudentServlet', //要请求的url地址
        type: 'POST', //请求方法 GET or POST
        async: true, //是否使用异步请求的方式
        timeout: 5000, //请求超时的时间，以毫秒计
        data:{
            method: 'addStudent',
            sno: $("#sno").val(),
            sname: $("#sname").val(),
            sdatebirth: $("#sdatebirth").val(),
            ssex: $("#ssex").val(),
            snativeplace: $("#snativeplace").val(),
            shouseaddress: $("#shouseaddress").val(),
            snation: $("#snation").val()
        },
        beforeSend: function (){//再发送请求前调用的方法

        },
        success: function (msg){//请求成功时回调方法，data为服务器返回的数据
            if(msg=="添加成功"){
                closeDialog()
            }
            alert(msg)
            clarJsonTable()
            load()
        },
        error: function (){//请求发生错误时调用方法
            alert("添加失败！")
        },
        complete: function (){//回调方法 无论success或者error都会执行

        }
    });
}
function deleteStudent(redSno){
    var num=''
    for(var i=0;i<3;i++){
        num+=Math.floor(Math.random()*10)
    }
    console.log(num);
    var kouling= prompt("请输入数字："+num+" 确认删除")
    if(kouling==num)
        $.ajax({
            url: 'StudentServlet', //要请求的url地址
            type: 'POST',
            async: true,
            timeout: 5000, //请求超时的时间，以毫秒计
            data:{
                method: 'deleteStudent',
                sno: redSno
            },
            beforeSend: function (){

            },
            success: function (msg){//请求成功时回调方法，data为服务器返回的数据
                alert(msg)
                clarJsonTable()
                load()
            },
            error: function (){//请求发生错误时调用方法
                alert("删除失败！")
            },
            complete: function (){

            },
        })
    else alert("删除失败");
}
function updateStudent(redSno){
    if ($(".el-dialog__wrapper").css("display") == "none"){
        $(".el-dialog__title").text("编辑")
        $("#dialog_yes").attr("onclick", "updateStudent("+redSno+")")
        $(".el-dialog__wrapper").show()
        $(".v-modal").show()
        var row_spans=$("#"+redSno).find("span")
        $("#sno").val(row_spans.eq(0).text()),
            $("#sname").val(row_spans.eq(1).text()),
            $("#sdatebirth").val(row_spans.eq(2).text()),
            $("#ssex").val(row_spans.eq(3).text()),
            $("#snativeplace").val(row_spans.eq(4).text()),
            $("#shouseaddress").val(row_spans.eq(5).text()),
            $("#snation").val(row_spans.eq(6).text())
        return
    }
    $.ajax({
        url: 'StudentServlet', //要请求的url地址
        type: 'POST',
        async: true, //是否使用异步请求的方式
        timeout: 5000, //请求超时的时间，以毫秒计
        data:{
            method: 'updateStudent',
            sno_old: redSno,
            sno: $("#sno").val(),
            sname: $("#sname").val(),
            sdatebirth: $("#sdatebirth").val(),
            ssex: $("#ssex").val(),
            snativeplace: $("#snativeplace").val(),
            shouseaddress: $("#shouseaddress").val(),
            snation: $("#snation").val()
        },
        beforeSend: function (){

        },
        success: function (msg){
            if(msg=="修改成功"){
                closeDialog()
            }
            alert(msg)
            clarJsonTable()
            load()
        },
        error: function (){
            alert("修改失败")
        },
        complete: function (){

        }
    });
}
function searResult(redSno){
    $.ajax({
        url: 'StudentServlet',
        type: 'POST',
        async: true,
        timeout: 5000,
        dataType: 'json',
        data:{
            method: "searResult",
            sno: redSno
        },
        beforeSend: function (){

        },
        success: function (msg){
            document.getElementById("Chinese").value=msg[0]['ch'];
            document.getElementById("math").value=msg[0]['ma'];
            document.getElementById("English").value=msg[0]['en'];
            document.getElementById("stop").innerHTML=msg[0]['mc'];
            document.getElementById("ssubject").innerHTML=msg[0]['km'];
        },
        error: function (){//请求发生错误时调用方法
            alert('查询失败')
        },
        complete: function (){

        }
    });
}
function searchStudent(){
    $.ajax({
        url: 'StudentServlet',
        type: 'POST',
        async: true,
        timeout: 5000,
        data:{
            method: "qureyStudent",
            key: $('#search').attr('key'),
            value: $('#search_text').val()
        },
        dataType: 'json',//预期的服务器返回参数类型
        beforeSend: function (){

        },
        success: function (json){
            if (json.length > 0){
                clarJsonTable()
                showStudent(json)
            }
        },
        error: function (msg){//请求发生错误时调用方法
            alert(msg.responseText)
        },
        complete: function (){

        }
    });
}
function queryStudent(e){
    $(".editForm").css("display", "inline")
    $.ajax({
        url: 'StudentServlet',
        type: 'POST',
        async: true,
        timeout: 5000,
        data:{
            method: "qureyStudent",
            key: "sno",
            value: e
        },
        dataType: 'json', //预期的服务器返回参数类型
        beforeSend: function (){

        },
        success: function (msg){
            $("#sno_new").val(msg[0]['sno'])
            $("#sno_new").attr("sno_old", msg[0]['sno'])
            $("#sname_new").val(msg[0]['sname'])
            $("#sage_new").val(msg[0]['sage'])
            $("#ssex_new").val(msg[0]['ssex'])
        },
        error: function (){
            alert("查询失败!")
        },
        complete: function (){

        }
    });
}
function load(){
    $.ajax({
        url: 'StudentServlet',
        type: 'POST',
        async: true,
        timeout: 5000,
        data:{
            method: 'loadStudent',
        },
        dataType: 'json',
        beforeSend: function (){

        },
        success: function (json){
            showStudent(json)
        },
        error: function (){
            alert("数据库读取失败")
        },
        complete: function (){

        }
    });
}
function clarJsonTable(){
    var element = document.getElementById("table_body");
    element.parentElement.removeChild(element);
}
function previous() {
    if(Page>=1){
        Page=Page-1
    }
    if (Page==0) {
        window.alert('当前为第一页，无法先前翻页')
        Page = Page + 1
    }
    else
    {
        $("#tableb tbody").html("");
        load();
    }
}
function next() {
    if(Page<=p) {
        Page = Page + 1
    }
    if (Page==(p+1)) {
        window.alert('当前为最后一页，无法向后翻页');
        Page = Page-1;
    }
    else {
        $("#tableb tbody").html("");
        load();
    }
}
function Go(){
    var v = document.getElementById("intext").value
    document.getElementById("intext").value=""
    var value=new Number(v)
    value=parseInt(value)
    if(value<1||value>p){
        window.alert('页码输入错误');
    }
    else{
        Page=value
        $("#tableb tbody").html("");
        load()
    }
}
function showStudent(json){
    var item = "<tbody id='table_body'>"
    var j;
    $.each(json, function (i, result) {
        j=i;
        if(i<=Page*6-Page-1 && i>=(Page-1)*6-Page+1)
            item += "<tr id='" + result['sno'] + "' class='el-table__row'><td rowspan='1' colspan='1' class='el-table_4_column_26 is-center '><div class='cell'><span>" + result['sno'] + "</span></div></td><td rowspan='1' colspan='1' class='el-table_4_column_27 is-center '><div class='cell'><span>" + result['sname'] + "</span></div></td><td rowspan='1' colspan='1' class='el-table_4_column_28  '><div class='cell'><span class='link-type'>" + result['sdatebirth'] + "</span> </div></td><td rowspan='1' colspan='1' class='el-table_4_column_29 is-center '><div class='cell'><span>" + result['ssex'] + "</span></div></td><td rowspan='1' colspan='1' class='el-table_4_column_30  '><div class='cell'><span class='link-type'>" + result['snativeplace'] + "</span></div></td><td rowspan='1' colspan='1' class='el-table_4_column_31 is-center '><div class='cell'><span class='link-type'>" + result['shouseaddress'] + "</span></div></td><td rowspan='1' colspan='1' class='el-table_4_column_32  status-col'><div class='cell'><span class='el-tag el-tag--info el-tag--medium'>" + result['snation'] + "</span></div></td><td rowspan='1' colspan='1' class='el-table_4_column_33 is-center small-padding fixed-width'><div class='cell'><button type='button' onclick='updateStudent(" + result['sno'] + ")' class='el-button el-button--primary el-button--mini'><span>编辑</span></button><button type='button' onclick='StuGrade(" + result['sno'] + ")' class='el-button el-button--primary el-button--mini'><span>成绩</span></button><button type='button'  onclick='deleteStudent(" + result['sno'] + ")' class='el-button el-button--dangerel-button--mini'><span>删除</span></button></div></td></tr>"
    })
    item += "</tbody>"
    $("#table_body_colgroup").after(item)
    document.getElementById("pageNum").innerHTML=Page
    if((j+1)%5==0)
        j=parseInt((j+1)/5);
    else j=parseInt((j+1)/5+1);
    p=j;
    document.getElementById("nums").innerHTML=j
}

