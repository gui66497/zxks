<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>题库管理</title>
    <link rel="stylesheet" type="text/css" href="../css/basic.css">
    <link rel="stylesheet" type="text/css" href="../css/bootstrap-treeview.css">
    <link rel="stylesheet" type="text/css" href="../css/itemList.css">
    <link rel="stylesheet" type="text/css" href="../css/itemBankManage.css">
    <link rel="stylesheet" type="text/css" href="../css/square/blue.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../exts/bootstrap/css/bootstrap-modal.css">
    <script type="application/javascript" src="../exts/jquery-1.12.3.min.js"></script>
    <script type="application/javascript" src="../exts/bootstrap-treeview.js"></script>
    <script type="application/javascript" src="../exts/icheck.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modal.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/bootstrap/js/bootstrap-modalmanager.js"></script>
    <script type="text/javascript" charset="utf8" src="../exts/modalTemplate.js"></script>
    <script type="text/javascript">

       //获取每个题库中题目类型占比详情
        /*
        var count1 = 0;
        var count2 = 0;
        var count3 = 0;
       function getBankInfo(){
            setTimeout(function(){
                $("[data-toggle='popover']").popover({
                    delay: {
                        show: 500,
                        hide: 100,
                    },
                    html:true,
                    content:function(){
                        var itemBankId = $(this).attr("itemBankId");
                        $.ajax({
                            contentType: "application/json",
                            dataType: "json",
                            headers: {"userId": 118},
                            type: "post",
                            url: "/zxks/rest/item/itemCountList",
                            data: JSON.stringify({
                                item: {
                                    itemBankId: itemBankId,
                                }
                            }),
                            success: function (data) {
                                var dataList = data.data;

                                var itemTypeStr = "";
                                count1 = 0;
                                count2 = 0;
                                count3 = 0;
                                var html = "";
                                $.each(dataList, function (index, item) {
                                    if (item.itemType == 1) {
                                        count1 += 1;
                                        itemTypeStr = "单选题";
                                    } else if (item.itemType == 2) {
                                        count2 += 1;
                                        itemTypeStr = "多选题";
                                    } else {
                                        count3 += 1;
                                        itemTypeStr = "判断题";
                                    }

                                });
                                if (dataList.length > 0) {
                                    html = "<table><thead><tr><th>题型</th><th>题目数</th><th>占比</th></tr></thead>" +
                                            "<tbody>" +
                                            "<tr><td>单选题</td><td>" + count1 + "</td><td>" + Math.round(count1 * 100 / (dataList.length)) + "%</td></tr>" +
                                            "<tr><td>多选题</td><td>" + count2 + "</td><td>" + Math.round(count2 * 100 / (dataList.length)) + "%</td></tr>" +
                                            "<tr><td>判断题</td><td>" + count3 + "</td><td>" + Math.round(count3 * 100 / (dataList.length)) + "%</td></tr>" +
                                            "</tbody></table>"



                                } else {
                                    html = "<div>该题库无题目</div>";
                                }
                                return html;

                            }

                        })
                    },
                    trigger: "hover",
                    placement: "top",
                });
            },1000)
        }
        $(function () {


        });*/

        var userId = <%=request.getSession().getAttribute("userId")%>;//用户ID
        var optionNames = ['A', 'B', 'C', 'D', 'E', 'F'];
        //树型目录
        function buildDomTree() {
            var data = [];
            var categories = [];
            $.ajax({
                url: "/zxks/rest/itemBank/categoryList",
                type: "get",
                contentType: "application/json; charset=UTF-8",
                async: false,
                dataType: "json",
                headers: {userId: userId},
                data: {},
                success: function (resultData) {
                    if (resultData != undefined) {
                        categories = resultData.data;
                        var topCategories = [];
                        var subCategories = [];
                        $.each(categories, function (index, category) {
                            if (category.parentId == 0) {
                                topCategories.push(category);
                            } else {
                                subCategories.push(category);
                            }
                        });
                        $.each(topCategories, function (index, category) {
                            $.each(subCategories, function (index2, subcategory) {
                                if (subcategory.parentId == category.categoryId) {
                                    if (!category.nodes) {
                                        category.nodes = []
                                    }
                                    var subNode = {
                                        categoryId: subcategory.categoryId,
                                        categoryName: subcategory.categoryName,
                                        counts: subcategory.counts
                                    };
                                    category.nodes.push(subNode);
                                    addNodes(subNode);
                                }

                            });
                        });

                        function addNodes(targetCategory) {
                            $.each(subCategories, function (index2, subcategory) {
                                if (subcategory.parentId == targetCategory.categoryId) {
                                    if (!targetCategory.nodes) {
                                        targetCategory.nodes = []
                                    }
                                    var subNode = {
                                        categoryId: subcategory.categoryId,
                                        categoryName: subcategory.categoryName,
                                        counts: subcategory.counts
                                    };
                                    targetCategory.nodes.push(subNode);
                                    addNodes(subNode);
                                }

                            });
                        }

                        function walk(topCategories, data) {
                            if (!topCategories) {
                                return;
                            }
                            $.each(topCategories, function (topIndex, topCategory) {
                                var obj = {}
                                if (topIndex == 0) {
                                    $('#nodeId').val(topCategory.categoryId);
                                    $('#categoryNameUpdate').val(topCategory.categoryName);
                                    $('#nodeText').val(topCategory.categoryName);
                                    showItemBankInfo(topCategory.categoryId);
                                    obj = {
                                        name:topCategory.categoryName,
                                        id: topCategory.categoryId,
                                        text: returnSpan(topCategory.categoryName, topCategory.counts),
                                        href: "#",
                                        state: {
                                            selected: true
                                        }
                                    };
                                } else {
                                    obj = {
                                        id: topCategory.categoryId,
                                        text: returnSpan(topCategory.categoryName, topCategory.counts),
                                        href: "#",
                                        name:topCategory.categoryName,
                                    };
                                }
                                if (topCategory.nodes != null) {
                                    obj.nodes = [];
                                    walk(topCategory.nodes, obj.nodes);
                                }
                                data.push(obj);
                            });
                        }

                        walk(topCategories, data);
                        var options = {
                            levels: 2,
                            data: data
                        };
                        $('#treeview').treeview(options);
                        $('#treeview').on('nodeSelected', function (event, data) {
                            $('#nodeId').val(data.id);
                            $('#nodeText').val(data.name);
                            showItemBankInfo(data.id);
                        });
                        $('#treeview').on('nodeUnselected', function (event, data) {
                            showItemBankInfo(0);
                            $('#nodeId').val("");
                            $('#categoryNameUpdate').val("");
                            $('#nodeText').val("");
                        });
                    }
                },
                error: function (xhr, r, e) {
                    showOkModal('提示框', e);
                }
            });
        }
        $(function () {
            $("#itemDiv").load("mymodal.jsp");//加载模态框模板页面
            buildDomTree();
            var elements = document.getElementsByClassName("node-selected");
            if (elements[0] == null) {
                $('#nodeId').val("");
                $('#categoryNameUpdate').val("");
            }

            $(".itemBankDiv").hover(function () {
                $("#allInfo").attr("display", "");
            });
        });

        function checkBoxDraw() {
            initCheckBox();
            /*$("#checkBoxAllCheckBox"+index).on("ifChecked", function (event) {
             console.log($('input[name="itemCheckbox'+index+'"]'));
             $('input[name="itemCheckbox'+index+'"]').iCheck("check");
             });
             $("#checkBoxAllCheckBox"+index).on("ifUnchecked", function (event) {
             console.log($('input[name="itemCheckbox'+index+'"]'));
             $('input[name="itemCheckbox'+index+'"]').iCheck("uncheck");
             });*/

            $('input[name="optionRadio"]').iCheck("check");
            $('input[name="optionRadio"]').iCheck("uncheck");


        }

        function initCheckBox() {
            $('input[type!=radio]').iCheck({
                checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                radioClass: 'iradio_square-blue',
                increaseArea: '20%' // optional
            })
        }
        function initRadioBox() {
            $('input[type=radio]').iCheck({
                checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系
                radioClass: 'iradio_square-blue',
                increaseArea: '20%' // optional
            })
        }

        function addItemBankCategory(){
            $('#categoryName').val('');
            $('#categoryAddModal').modal({
                show: true,
                keyboard: false,
                backdrop: 'static'
            })
            //延迟添加focus时间
            setTimeout(function(){
                $('#categoryName').focus();
            },800);

        }
        // 确定删除分类模态框
        function showCategoryDeleteModal(fn) {
            var nodeId = $('#nodeId').val();
            if (nodeId == "") {
                $('#warnTip').text('请先选择分类!');
                setTimeout(function(){
                    $('#warnModal').modal(
                            {
                                show: true,
                                keyboard: false,
                                backdrop: 'static'
                            }
                    );
                },200);
                return false;
            }
            $('#sureTip').text('确定要删除吗？');
            $('#btnExecute').attr('onclick', fn);
            setTimeout(function(){
                $('#sureModal').modal(
                        {
                            show: true,
                            keyboard: false,
                            backdrop: 'static'
                        }
                );
            },200);
        }
        //删除分类
        function deleteCategory() {
            var nodeId = $('#nodeId').val();
            $.ajax({
                url: "/zxks/rest/itemBank/categoryDelete",
                type: "DELETE",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({id: nodeId}),
                success: function (resultData) {
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    });
                        },200);
                        if (resultData.resultCode == 'SUCCESS') {
                            buildDomTree();
                            /*$('#nodeId').val("");
                            $('#categoryNameUpdate').val("");
                            $('#nodeText').val("");*/
                        }
                    }
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                }
            });
        }

        //添加分类
        function addCategory() {
            var nodeId = $('#nodeId').val();
            var categoryName = $('#categoryName').val().trim();
            var categoryNameReg = /^[\u4e00-\u9fa5#a-zA-Z0-9\s{1,}]+$/; //正则,中文、英文、数字及下划线
            if (categoryName == '') {
                showOkModal("提示框","请填写分类名称!");
                //showAlert('addCategoryWarn', '请填写分类名称!');
                $('#categoryName').focus();
                return false;
            }
            if (!categoryNameReg.test(categoryName)) {
                showOkModal("提示框","分类名称格式不对!只支持中文、英文、数字、空格及#!");
                //showAlert('addCategoryWarn', '分类名称格式不对!只支持中文、英文、数字及#!');
                $('#categoryName').focus();
                return false;
            }
            nodeId = "0";
            $.ajax({
                url: "/zxks/rest/itemBank/category",
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"itemBankCategory": {"parentId": nodeId, "categoryName": categoryName}}),
                success: function (resultData) {
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        },200);
                    }
                    buildDomTree();//重新加载分类
                    /*$('#nodeId').val("");
                    $('#nodeText').val("");*/
                    //$('#categoryName').val("");
                    $('#categoryAddModal').modal('hide');
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                    $('#nodeId').val("");
                    $('#nodeText').val("");
                    $('#categoryName').val("");
                    $('#categoryAddModal').modal('hide');
                }
            });
        }

        //显示分类更新模态框
        function showCategoryUpdateModal() {

            var nodeId = $('#nodeId').val();
            if (nodeId == "0" || nodeId == "") {
                $('#warnTip').text("请先选择分类!");
                setTimeout(function(){
                    $('#warnModal').modal(
                            {
                                show: true,
                                keyboard: false,
                                backdrop: 'static'
                            }
                    );
                },200);
            } else {
                $('#categoryNameUpdate').val($('#nodeText').val());
                setTimeout(function(){
                    $('#categoryUpdateModal').modal(
                            {
                                show: true,
                                keyboard: false,
                                backdrop: 'static'
                            }
                    );
                },200);
            }
            //延迟添加focus时间
            setTimeout(function () {
                $('#categoryNameUpdate').focus();
            }, 800);
        }

        //更新分类
        function updateCategory() {
            var nodeId = $('#nodeId').val().trim();
            var nodeText = $('#nodeText').val().trim();
            var categoryName = $('#categoryNameUpdate').val().trim();
            var categoryNameReg = /^[\u4e00-\u9fa5#a-zA-Z0-9\s{1,}]+$/; //正则,中文、英文、数字及下划线
            if (categoryName == '') {
                showOkModal("提示框","请填写分类名称!");
                //showAlert('updateCategoryWarn', '请填写分类名称!');
                $('#categoryNameUpdate').focus();
                return false;
            }
            if (!categoryNameReg.test(categoryName)) {
                showOkModal("提示框","分类名称格式不对!只支持中文、英文、数字及#!");
                //showAlert('updateCategoryWarn', '分类名称格式不对!只支持中文、英文、数字及#!');
                $('#categoryNameUpdate').focus();
                return false;
            }
            if (nodeId == "") {
                nodeId = "0";
            }
            if (nodeText == categoryName) {
                showOkModal("提示框","分类名称未修改!");
                //showAlert('updateCategoryWarn', '分类名称未修改!');
                $('#categoryNameUpdate').focus();
                return false;
            }
            $.ajax({
                url: "/zxks/rest/itemBank/categoryUpdate",
                type: "PUT",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"itemBankCategory": {"categoryId": nodeId, "categoryName": categoryName}}),
                success: function (resultData) {
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        },200);
                    }
                    buildDomTree();//重新加载分类
                    $/*('#nodeId').val("");
                    $('#nodeText').val("");
                    $('#categoryNameUpdate').val("");*/
                    $('#categoryUpdateModal').modal('hide');
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                    $('#nodeId').val("");
                    $('#nodeText').val("");
                    $('#categoryNameUpdate').val("");
                    $('#categoryUpdateModal').modal('hide');
                }
            });
        }
        //添加title提示
        function returnSpan(targetStr, counts) {
            var name = "";
            if (targetStr.length > 10) {
                name = targetStr.substr(0,10) + "...";
            } else {
                name = targetStr;
            }

            return "<span title='" + targetStr + "' class='ellipsis' style='white-space: pre-wrap;float:left;margin-left:10px;' >" + name + " </span><span class='badge' style='float:right;margin-top: 10px;background-color:#4dbd73;margin-right:4px;'>"+counts+"</span>";
        }

        function returnItemTitleSpan(targetStr) {
            var name = "";
            if (targetStr.length > 10) {
                name = targetStr.substr(0,10) + "...";
            } else {
                name = targetStr;
            }

            return "<span title='" + targetStr + "' class='ellipsis'  >" + name + " </span>";
        }
        //显示创建题库模态框
        function showItemBankInfoAddModal() {
            var categories = [];
            var categoryId = $('#nodeId').val();
            $.ajax({
                url: "/zxks/rest/itemBank/categoryList",
                type: "get",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: {},
                success: function (resultData) {
                    categories = resultData.data;
                    var optionHtml = "";
                    $.each(categories, function (index, category) {
                        //添加下拉框
                        if (categoryId == category.categoryId) {
                            optionHtml += "<option value='" + category.categoryId + "' selected='selected'>" + category.categoryName + "</option>";
                        } else {
                            optionHtml += "<option value='" + category.categoryId + "'>" + category.categoryName + "</option>";
                        }
                    });
                    //延迟添加focus时间
                    setTimeout(function () {
                        $('#itemBankName').focus();
                    }, 800);
                    $('#itemBankCategorySelect').text('');
                    $('#itemBankCategorySelect').append(optionHtml);
                    var itemBankName = $('#itemBankName').val('');
                    var itemBankDescription = $('#itemBankDescription').val('');
                    setTimeout(function(){
                        $('#itemBankInfoAddModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);

                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);

                }
            });
        }

        //添加题库信息
        function addItemBank() {
            var itemBankNameReg = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/ //题库名称正则,中文、英文、数字及下划线
            var categoryId = $('#nodeId').val().trim();
            var nodeId = $('#itemBankCategorySelect option:selected').val().trim();
            var itemBankName = $('#itemBankName').val().trim();
            var itemBankDescription = $('#itemBankDescription').val().trim();
            if (nodeId == "") {
                showAlert('addItemBankWarn', '请选择题库分类!');
                return false;
            }
            if (itemBankName == "") {
                showAlert('addItemBankWarn', '请填写题库名称!');
                $('#itemBankName').focus();
                return false;
            }
            if (itemBankName != "" && !itemBankNameReg.test(itemBankName)) {
                showAlert('addItemBankWarn', '题库名称格式不对!只支持中文、英文、数字及下划线!');
                $('#itemBankName').focus();
                return false;
            }
            $.ajax({
                url: "/zxks/rest/itemBank/infoAdd",
                type: "POST",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({
                    "itemBank": {
                        "categoryId": nodeId,
                        "itemBankName": itemBankName,
                        "itemBankDescription": itemBankDescription
                    }
                }),
                success: function (resultData) {
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        },200);
                    }
                    $('#nodeText').val("");
                    $('#categoryName').val("");
                    $('#itemBankInfoAddModal').modal('hide');
                    if (categoryId == nodeId) {
                        showItemBankInfo(nodeId);//显示题库信息
                    }
                    //刷新左侧树
                    refreshDomTree(nodeId)
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                    $('#nodeText').val("");
                    $('#categoryName').val("");
                    $('#itemBankInfoAddModal').modal('hide');
                }
            });
        }

        //显示题库列表
        function showItemBankInfo(categoryId) {
            $('#searchName').val('');
            var itemBankList = [];
            $.ajax({
                url: "/zxks/rest/itemBank/infoList",
                type: "get",
                contentType: "application/json; charset=UTF-8",
                async: false,
                dataType: "json",
                headers: {userId: userId},
                data: {"id": categoryId},
                success: function (resultData) {
                    itemBankList = resultData.data; //获取题库信息
                    var itemInfo = "";
                    if (itemBankList != null) {
                        $.each(itemBankList, function (index, itemBankInfo) {
                            var itemBankDivStyle;
                            if ((index + 1) % 4 == 0) {
                                itemBankDivStyle = 'style="margin-right: 12px;"';
                            }
                            itemInfo += '<div id="allInfo" style="float: left;margin-top: -30px;display: none;"><table id="showTable' + itemBankInfo.itemBankId + '"></table></div>'
                            itemInfo += '<div class="itemBankDiv" ' + itemBankDivStyle + 'data-placement="top" data-toggle="popover"  itemBankId = "'+itemBankInfo.itemBankId+'">';
                            itemInfo += '<div class="itemBankHeader">';
                            itemInfo += '<div class="itemBankTitle" onclick="getItemList(' + escape(itemBankInfo.itemBankName) + ',' + itemBankInfo.itemBankId + ',' + categoryId + ')">' +
                                    '<a href="itemList.jsp?itemBankName=' + escape(itemBankInfo.itemBankName) +
                                    '&itemBankId=' + itemBankInfo.itemBankId + '&itemCategoryId=' + categoryId + '">' + returnItemTitleSpan(itemBankInfo.itemBankName) + '</a>';
                            itemInfo += '</div>';
                            itemInfo += '<span class="pull-right dropdown" style="margin-right: 15px;font-size: 14px;margin-top: -20px;">';
                            itemInfo += '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">';
                            itemInfo += '<i class="icon-chevron-down icon-white"></i>';
                            itemInfo += '</a>';
                            itemInfo += '<ul class="dropdown-menu">';
                            var itemBankDescription = itemBankInfo.itemBankDescription.replace(new RegExp("\n","g"),"<br\>");
                            itemInfo += '<input type ="text" id="itemBankId'+itemBankInfo.itemBankId+'" value="'+itemBankDescription+'" style="display:none" >'
                            itemInfo += '<li class="itemBankLi"><a onclick="showItemBankInfoUpdateModal('+categoryId+','+itemBankInfo.itemBankId+',\''+itemBankInfo.itemBankName+'\')">编辑题库</a></li>';
                            itemInfo += '<li><a onclick="showItemBankDeleteModal(' + itemBankInfo.itemBankId + ',' + categoryId + ')">删除题库</a></li>';
                            /*  itemInfo += '<li class="divider"></li>';*/
                            /* itemInfo += '<li><a href="#">文件导入题目</a></li>';*/
                            itemInfo += '</ul>';
                            itemInfo += '</span>';
                            itemInfo += '<div>更新于<span style="color:#ccd3dd">' + itemBankInfo.updateTime + '</span></div>';
                            itemInfo += '</div>';
                            itemInfo += '<div style="padding-left: 15px">';
                            itemInfo += '<div style="padding-top: 15px;float: left;color: #000"><span style="color: #fc6363;font-size: 16px;font-weight: bolder">' + itemBankInfo.itemCount + '</span>&nbsp;道题</div>';
                            itemInfo += '<div style="padding:15px 15px;float: right;"><a href="#" style="color: #3bc0bf" onclick="showItemAddModal(' + itemBankInfo.itemBankId + ')">添加题目</a></div>';
                            itemInfo += '</div>';
                            itemInfo += '</div>';
                        });
                    } else {
                        itemInfo = '<span class="itemBankTitle" style="font-family: 微软雅黑">数据为空</span>';
                    }
                    $('#itemBankInfo').text('');
                    $('#itemBankInfo').append(itemInfo);
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                }
            });
        }

        function getItemList(itemBankName, itemBankId, categoryId) {
            window.location.href = "itemList.jsp?itemBankName=" + itemBankName + "&itemBankId=" + itemBankId + "&itemCategoryId=" + categoryId + "";
        }
        // 确定删除分类模态框
        function showItemBankDeleteModal(itemBankId, categoryId, fn) {
            $('#itemBankId').val(itemBankId);
            $('#nodeId').val(categoryId);
            $('#sureTip').text('确定要删除吗？');
            $('#btnExecute').attr('onclick', 'deleItemBankInfo()');
            setTimeout(function(){
                $('#sureModal').modal(
                        {
                            show: true,
                            keyboard: false,
                            backdrop: 'static'
                        }
                );
            },200);
        }

        //根据题库信息删除题库信息
        function deleItemBankInfo() {
            var itemBankId = $('#itemBankId').val();
            var categoryId = $('#nodeId').val();
            $.ajax({
                url: "/zxks/rest/itemBank/infoDelete",
                type: "DELETE",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"id": itemBankId}),
                success: function (resultData) {
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        },200);
                    }
                    showItemBankInfo(categoryId);//显示题库信息
                    //刷新左侧树
                    refreshDomTree(categoryId);
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                }
            });
        }

        //修改题库信息
        function updateItemBankInfo() {
            var itemBankNameReg = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/ //题库名称正则,中文、英文、数字及下划线
            var nodeId = $('#itemBankCategoryUpdateSelect option:selected').val().trim();
            var iteBankId = $('#itemBankId').val().trim();
            var itemBankName = $('#itemBankNameUpdate').val().trim();
            var itemBankDescription = $('#itemBankDescriptionUpdate').val().trim();
            if (nodeId == "") {
                showAlert('updateItemBankWarn', '请填写选择题库分类!');
                return false;
            }
            if (itemBankName == "") {
                showAlert('updateItemBankWarn', '请填写题库名称!');
                $('#itemBankName').focus();
                return false;
            }
            if (itemBankName != "" && !itemBankNameReg.test(itemBankName)) {
                showAlert('updateItemBankWarn', '题库名称格式不对!只支持中文、英文、数字及下划线!');
                $('#itemBankName').focus();
                return false;
            }
            $.ajax({
                url: "/zxks/rest/itemBank/infoUpdate",
                type: "PUT",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({
                    "itemBank": {
                        "itemBankId": iteBankId,
                        "categoryId": nodeId,
                        "itemBankName": itemBankName,
                        "itemBankDescription": itemBankDescription
                    }
                }),
                success: function (resultData) {
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        },200);
                    }
                    $('#nodeText').val("");
                    $('#categoryName').val("");
                    $('#itemBankInfoUpdateModal').modal('hide');
                    showItemBankInfo(nodeId);//显示题库信息
                    //刷新左侧树
                    refreshDomTree(nodeId)
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                    $('#nodeText').val("");
                    $('#categoryName').val("");
                    $('#itemBankInfoUpdateModal').modal('hide');
                }
            });
        }

        //刷新左侧树状表格并选中指定节点
        function refreshDomTree(nodeId) {
            buildDomTree();
            $("#treeview").treeview('uncheckAll');
            var nodes = $("#treeview").treeview('getUnchecked');
            $.each(nodes, function (i, node) {
                if (nodeId == node.id) {
                    $("#treeview").treeview('selectNode', node.nodeId);
                }
            });
        }

        //查询题库信息
        function searchItemBankInfo(searchName) {
            var categoryId = $('#nodeId').val();
            var itemBankList = [];
            $.ajax({
                url: "/zxks/rest/itemBank/infoList",
                type: "get",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: {"id": categoryId, "searchName": searchName},
                success: function (resultData) {
                    itemBankList = resultData.data; //获取题库信息
                    var itemInfo = "";
                    if (itemBankList != null) {
                        $.each(itemBankList, function (index, itemBankInfo) {
                            var itemBankDivStyle;
                            if ((index + 1) % 4 == 0) {
                                itemBankDivStyle = 'style="margin-right: 12px;"';
                            }
                            itemInfo += '<div class="itemBankDiv" ' + itemBankDivStyle + ' >';
                            itemInfo += '<div class="itemBankHeader">';
                            itemInfo += '<div class="itemBankTitle" onclick="getItemList(' + encodeURI(encodeURI(itemBankInfo.itemBankName)) + ',' + itemBankInfo.itemBankId + ',' + categoryId + ')">' +
                                    '<a href="itemList.jsp?itemBankName=' + encodeURI(encodeURI(itemBankInfo.itemBankName)) +
                                    '&itemBankId=' + itemBankInfo.itemBankId + '&itemCategoryId=' + categoryId + '">' + returnItemTitleSpan(itemBankInfo.itemBankName) + '</a>';
                            itemInfo += '</div>';
                            itemInfo += '<span class="pull-right dropdown" style="margin-right: 15px;font-size: 14px;margin-top: -20px;">';
                            itemInfo += '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">';
                            itemInfo += '<i class="icon-chevron-down icon-white"></i>';
                            itemInfo += '</a>';
                            itemInfo += '<ul class="dropdown-menu">';
                            itemInfo += '<li class="itemBankLi"><a onclick="showItemBankInfoUpdateModal(' + categoryId + ',' + itemBankInfo.itemBankId + ',\'' + itemBankInfo.itemBankName + '\',\'' + itemBankInfo.itemBankDescription + '\')">编辑题库</a></li>';
                            itemInfo += '<li><a onclick="showItemBankDeleteModal(' + itemBankInfo.itemBankId + ',' + categoryId + ')">删除题库</a></li>';
                            itemInfo += '<li class="divider"></li>';
                            /* itemInfo += '<li><a href="#">文件导入题目</a></li>';*/
                            itemInfo += '</ul>';
                            itemInfo += '</span>';
                            itemInfo += '<div>更新于' + itemBankInfo.updateTime + '</div>';
                            itemInfo += '</div>';
                            itemInfo += '<div style="padding-left: 15px">';
                            itemInfo += '<div style="padding-top: 15px;float: left;color: #000"><span style="color: #fc6363;font-size: 16px;font-weight: bolder">' + itemBankInfo.itemCount + '</span>&nbsp;道题</div>';
                            itemInfo += '<div style="padding:15px 15px;float: right;"><a href="#" style="color: #3bc0bf" onclick="showItemAddModal(' + itemBankInfo.itemBankId + ')">添加题目</a></div>';
                            itemInfo += '</div>';
                            itemInfo += '</div>';
                        });
                    } else {
                        itemInfo = '数据为空';
                    }
                    $('#itemBankInfo').text('');
                    $('#itemBankInfo').append(itemInfo);
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                }
            });
        }

        //显示更新题库模态框
        function showItemBankInfoUpdateModal(categoryId, itemBankId, itemBankName) {
            //延迟添加focus事件
            setTimeout(function () {
                $('#itemBankNameUpdate').focus();
            }, 800);
            var itemBankDescription = $("#itemBankId"+itemBankId).val();
            itemBankDescription = itemBankDescription.replace(new RegExp("<br>","g"),"\n");
            var categories = [];
            $('#itemBankId').val(itemBankId);
            $.ajax({
                url: "/zxks/rest/itemBank/categoryList",
                type: "get",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: {},
                success: function (resultData) {
                    categories = resultData.data;
                    var optionHtml = "";
                    $.each(categories, function (index, category) {
                        //添加下拉框
                        if (categoryId == category.categoryId) {
                            optionHtml += "<option value='" + category.categoryId + "' selected='selected'>" + category.categoryName + "</option>";
                        } else {
                            optionHtml += "<option value='" + category.categoryId + "'>" + category.categoryName + "</option>";
                        }

                    });
                    $('#itemBankCategoryUpdateSelect').text('');
                    $('#itemBankCategoryUpdateSelect').append(optionHtml);
                    $('#itemBankNameUpdate').val(itemBankName);
                    $('#itemBankDescriptionUpdate').val(itemBankDescription);
                    setTimeout(function(){
                        $('#itemBankInfoUpdateModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                }
            });
        }

        //显示添加题目模态框
        function showItemAddModal(itemBankId) {
            $('#itemTypeSelect').val(1);//初始化题型
            $('#itemTitle').val('');//清空题干
            $('#itemCode').val('');//清空题目代码
            $('#answerSolution').val('');//清空答案解析
            changeType(1);//加载添加题目选项
            //延迟添加focus事件
            setTimeout(function () {
                $('#itemTitle').focus();
            }, 1000);
            $('#itemBankId').val(itemBankId);
            setTimeout(function(){
                $('#itemAddModal').modal(
                        {
                            show: true,
                            keyboard: false,
                            backdrop: 'static'
                        });
            },200);

        }

        //添加题目信息
        function addItemInfo() {
            var itemOptions = [];
            var itemId = new Date().getTime();//获取时间戳作为题目编号
            var itemBankId = $('#itemBankId').val();//题库Id
            var itemType = $('#itemTypeSelect option:selected').val();//题目类型
            var itemTitle = $('#itemTitle').val().trim();//题干
            if (itemTitle == '') {
                showOkModal('提示框', '请填写题干内容!');
                $('#itemTitle').focus();
                return false;
            }
            var itemCode = $('#itemCode').val().trim();//题目代码
            var elements = document.getElementsByName("optionRadio");//题目选项元素列表
            var contentWarn;
            var contents = [];//创建内容数组便于重复校验使用
            var flag = false;
            $.each(elements, function (index, element) {
                var optionId = element.value;
                var content = $('#optionInput' + optionId).val();
                content = content.trim();
                if ($.inArray(content, contents) > -1) {
                    flag = true;
                    return;
                } else {
                    contents.push(content);
                }
                if (content == '') {
                    contentWarn = true;
                    return false;
                }
                itemOptions.push({
                    "optionId": optionId,
                    "content": content,
                    "itemId": itemId,
                    "creator": userId
                });
            });
            if (contentWarn) {
                showOkModal('提示框', '请填写全部选项内容!');
                return false;
            }
            if (flag) {
                showOkModal('提示框', '选项存在重复!');
                return false;
            }
            var answer = '';//答案
            if (itemType == 2) {
                $('input[type="checkbox"][name="optionRadio"]:checked').each(function (index, element) {
                    answer += $(element).val() + ",";
                });
                if (answer.indexOf(",") > -1) {
                    answer = answer.substr(0, answer.lastIndexOf(","));
                }
                if (answer.indexOf(",") == -1) {
                    showOkModal('提示框', '请选择2个以上答案!');
                    return false;
                }
            } else if (itemType == 4) {
                answer = $('#optionInput1').val();
                if (answer == undefined) {
                    showAlert('addItemWarn', '请填写答案!');
                    return false;
                }
            } else if (itemType == 5) {
                var inputs = $("input[name='optionInput']");
                if (inputs.length == 0) {
                    showAlert('addItemWarn', '请添加填空项!');
                    return false;
                }
                var answerArr = [];
                var tag = true;
                $.each(inputs, function (i, input) {
                    var oneAnswer = $(this).val();
                    if (oneAnswer.trim() == '') {
                        showAlert('addItemWarn', '填空项答案不能为空!');
                        tag = false;
                        return false;
                    }
                    answerArr.push(oneAnswer);
                });
                if (!tag) {
                    return;
                }
                console.log(itemTitle.split("(░░)").length - 1)
                var titleTagLength = itemTitle.split("(░░)").length - 1;
                if (titleTagLength != answerArr.length) {
                    showAlert('addItemWarn', '题干填空标识数量和填空项数目不一致!');
                    return;
                }
                answer = answerArr.join("░");
                console.log(answer);

            } else {
                answer = $('input[type="radio"][name="optionRadio"]:checked').val();
                if (answer == undefined) {
                    showOkModal('提示框', '请选择答案!');
                    return false;
                }
            }
            var answerResolution = $('#answerSolution').val().trim();
            var item = {
                "itemId": itemId,
                "itemTitle": itemTitle,
                "itemCode": itemCode,
                "answer": answer,
                "answerResolution": answerResolution,
                "itemType": itemType,
                "itemBankId": itemBankId,
                "options": itemOptions
            };
            $.ajax({
                url: "/zxks/rest/item/itemAdd",
                type: "post",
                contentType: "application/json; charset=UTF-8",
                async: true,
                dataType: "json",
                headers: {userId: userId},
                data: JSON.stringify({"item": item}),
                success: function (resultData) {
                    showItemBankInfo($('#nodeId').val());//刷新题库面板信息
                    if (resultData != null) {
                        $('#warnTip').text(resultData.message);
                        setTimeout(function(){
                            $('#warnModal').modal(
                                    {
                                        show: true,
                                        keyboard: false,
                                        backdrop: 'static'
                                    }
                            );
                        },200);
                    }
                    $('#nodeText').val("");
                    $('#categoryName').val("");
                    $('#itemAddModal').modal('hide');
                },
                error: function (xhr, r, e) {
                    $('#warnTip').text(e);
                    setTimeout(function(){
                        $('#warnModal').modal(
                                {
                                    show: true,
                                    keyboard: false,
                                    backdrop: 'static'
                                }
                        );
                    },200);
                }
            });
        }
        //回车键查询事件
        function keyDown(e) {
            /*var ev = window.event || e;
            //13是键盘上面固定的回车键
            if (ev.keyCode == 13) {
                searchItemBankInfo($('#searchName').val());
            }*/
            searchItemBankInfo($('#searchName').val());
        }
        //题目类型更改选项
        function changeType(type) {
            var optionHtml = '';
            var nums = [1, 2, 3, 4];
            if (type == 1) {
                $('#optionTip').text('正确选项个数为1');
                $.each(nums, function (index, num) {
                    optionHtml += '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + num + '">' +
                            '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                            '<input name="optionRadio" id="optionRadio' + num + '" type="radio" value="' + num + '" style="margin-top:0;margin-right:10px;">' +
                            '<span name="optionName" id="optionName' + num + '">' + optionNames[num - 1] + '</span></label>' +
                            '<input class="inputItem" maxlength="200" style="width: 520px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + num + '" placeholder="请填写选项内容"/>' +
                            '<a href="#" class="optionA" name="optionA" id="optionA' + num + '" onclick="changeOption(' + num + ')">&times;</a>' +
                            '</div>';
                });
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px');
                $('#addOptionBtn').attr('onclick', 'addOption(1)');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);
            } else if (type == 2) {
                $('#optionTip').text('正确选项个数为2个及以上');
                $.each(nums, function (index, num) {
                    optionHtml += '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + num + '">' +
                            '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                            '<input name="optionRadio" id="optionRadio' + num + '" type="checkbox" value="' + num + '" style="margin-top: -2px;margin-right:10px;">' +
                            '<span name="optionName" id="optionName' + num + '">' + optionNames[num - 1] + '</span></label>' +
                            '<input class="inputItem" maxlength="200" style="width: 520px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + num + '" placeholder="请填写选项内容"/>' +
                            '<a href="#" class="optionA" name="optionA" id="optionA' + num + '" onclick="changeOption(' + num + ')">&times;</a>' +
                            '</div>';
                });
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px');
                $('#addOptionBtn').attr('onclick', 'addOption(2)');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);
            } else if (type == 3) {
                $('#optionTip').text('正确选项个数为1');
                optionHtml += '<div style="margin-top: 0px;">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                        '<input name="optionRadio" id="optionRadio1" type="radio" value="1" style="margin-top:0;margin-right:10px;">' +
                        'A</label>' +
                        '<input class="inputItem" maxlength="200" style="width: 530px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput1" placeholder="请填写选项内容" value="对" disabled="disabled"/>' +
                        '</div>';
                optionHtml += '<div style="margin-top: 0px;">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                        '<input name="optionRadio" id="optionRadio2" type="radio" value="2" style="margin-top:0;margin-right:10px;">' +
                        'B</label>' +
                        '<input class="inputItem" maxlength="200" style="width: 530px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput2" placeholder="请填写选项内容" value="错" disabled="disabled"/>' +
                        '</div>';
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px;display: none');
                $('#optionDiv').text('');

                $('#optionDiv').append(optionHtml);

            } else if (type == 4) {
                $('#optionTip').text('请填写正确答案');
                optionHtml += '<div style="margin-top: 15px;">' +
                        '<input name="optionRadio" id="optionRadio1" type="hidden" value="1">' +
                        '<textarea class="shortAnswer" id="optionInput1" rows="2" placeholder="请填写答案内容"></textarea>' +
                        '</div>';
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px;display: none');
                $('#optionDiv').text('');
                $('#optionDiv').append(optionHtml);

            } else if (type == 5) {
                $.each(nums, function (index, num) {
                    optionHtml += '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + num + '">' +
                            '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                            '<span name="optionName" id="optionName' + num + '">' + optionNames[num - 1] + '</span></label>' +
                            '<input class="inputItem" style="width: 500px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + num + '" maxlength="200" placeholder="请填写选项内容"/>' +
                            '</div>';
                });
                $('#addOptionBtnDiv').attr('style', 'margin-top: 20px');
                $('#addOptionBtn').attr('onclick', 'addOption(5)');
                $('#optionTip').text('请填写每个填空的正确答案');
                $('#optionDiv').text('');
            }
        }
        //删除选项，相应更新其他选项信息，传入选项id信息
        function changeOption(id) {
            //填空题
            if (type == 5) {
                var elements1 = $('span[name=optionName]');
                var length1 = elements1.length;
                var newValue1 = length1 + 1;
                var optionHtml1 = '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + newValue + '">' +
                        '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                        '<span name="optionName" id="optionName' + newValue1 + '">' + '填空' + newValue1 + '</span></label>' +
                        '<input name="optionRadio" id="optionRadio' + newValue1 + '" type="hidden" value="' + newValue1 + '" style="margin-top: -2px;margin-right:10px;">' +
                        '<input class="inputItem" style="width: 520px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + newValue1 + '" placeholder="请填写填空内容"/>' +
                        '</div>';
                $('#optionDiv').append(optionHtml1);

                var titleText = $("#itemTitle").val();
                titleText += "(░░)";
                $("#itemTitle").val(titleText)
                return;
            }
            var elements = $('input[name=optionRadio]');
            var length = elements.length;
            if (length <= 3) {
                showOkModal('提示框', '最少3个选项!');
                return false;
            }
            $('#optionDiv' + id).remove();
            changeElements(id, 'id', 'optionDiv');
            changeElements(id, 'value', 'optionRadio');
            changeElements(id, 'id', 'optionRadio');
            changeElements(id, 'id', 'optionInput');
            changeElements(id, 'onclick', 'optionA');
            changeElements(id, 'id', 'optionA');
            changeElements(id, 'id', 'optionName');
        }


        //删除选项，相应更新其他选项信息
        function changeElements(value, attName, elementName) {
            var elements = $('[name=' + elementName + ']');
            $.each(elements, function (index, element) {
                var oldId = Number(index + value + 1);
                if (oldId > value) {
                    var newId = Number(oldId - 1);
                    if (attName == 'value') {
                        $('#' + elementName + oldId).val("" + newId + "");
                    } else {
                        if (attName != 'id' && elementName == 'optionA') {
                            $('#' + elementName + oldId).attr(attName, 'changeOption(' + newId + ')');
                        } else {
                            $('#' + elementName + oldId).attr(attName, elementName + newId);
                            if (elementName == 'optionName') {
                                $('#' + elementName + newId).text(optionNames[newId - 1]);
                            }
                        }
                    }
                }
            });
        }
        //添加选项，相应更新选项信息
        function addOption(type) {
            var elements = $('input[name=optionRadio]');
            var length = elements.length;
            if (length >= 6) {
                showOkModal('提示框', '最多添加6个选项!');
                return false;
            }
            var newValue = Number($('#optionRadio' + Number(length)).val()) + 1;
            var inputType = 'radio';
            if (type == 2) {
                inputType = 'checkbox';
            }
            var optionHtml = '<div style="margin-top: 0px;" name="optionDiv" id="optionDiv' + newValue + '">' +
                    '<label style="margin-left: 10px;margin-right: 20px;width: 40px;line-height:30px;text-align: left;">' +
                    '<input name="optionRadio" id="optionRadio' + newValue + '" type="' + inputType + '" value="' + newValue + '" style="margin-top:0;margin-right:10px;">' +
                    '<span name="optionName" id="optionName' + newValue + '">' + optionNames[newValue - 1] + '</span></label>' +
                    '<input class="inputItem" maxlength="200" style="width: 520px;margin-left: 70px;margin-top: -35px;" name="optionInput" id="optionInput' + newValue + '" placeholder="请填写选项内容"/>' +
                    '<a href="#" class="optionA" name="optionA" id="optionA' + newValue + '" onclick="changeOption(' + newValue + ')" >&times;</a>' +
                    '</div>';
            $('#optionDiv').append(optionHtml);
        }
    </script>
    <style>
        th, button, td, span, textarea[placeholder], select, input, div, ul {

            font-family: 微软雅黑 !important;
        }


        .control-label {
            position: absolute;
        }

        .labelObj {
            position: relative;
        }

        input[type="password"] {
            height: 34px !important;
            width: 150px !important;
            margin-left: 160px;
            margin-top: 12px;
        }
        #update_password_modal{
            top: 30% !important;
            height:270px;
        }
    </style>
</head>
<body style="background-color: #f1f4f8">
<input id="nodeId" value="" style="display: none"/>
<input id="nodeText" value="" style="display: none"/>
<input id="itemBankId" value="" style="display: none"/>

<div id="mainDiv">
    <div id="headDiv">
        <jsp:include page="menu.jsp"/>
    </div>
    <div id="contentDiv" style="text-align: left;">
        <div class="container-fluid">
            <div class="leftDiv">
                <div class="categoryDiv">
                    <div class="treeDiv">
                        <span class="fontSpan">题库分类</span>

                        <div class="btn-group" style="margin:0 0 3px 0;float: right">
                            <button class="btn btn-default btn-xs"
                                    title="新增类别"

                                    data-placement="auto left"
                                    data-trigger="hover focus"
                                    onclick="addItemBankCategory()">
                                <span class="icon-plus"></span>
                            </button>
                            <button class="btn btn-default btn-xs"
                                    title="删除类别"

                                    data-placement="auto bottom"
                                    data-trigger="hover focus"
                                    onclick="showCategoryDeleteModal('deleteCategory()');">
                                <span class="icon-minus"></span>
                            </button>
                            <button class="btn btn-default btn-xs"
                                    title="编辑类别"
                                    data-placement="auto right"
                                    data-trigger="hover focus"
                                    data-toggle="modal"
                                    onclick="showCategoryUpdateModal()"
                                    >
                                <span class="icon-pencil"></span>
                            </button>
                        </div>
                        <div id="treeview" class="treeview"></div>
                    </div>
                </div>
            </div>
            <div class="rightDiv">
                <div class="rightHeader"><span style="margin-left: 15px;font-size: 16px">题库管理</span></div>
                <div style="float: left">
                    <button class="addItemBtn" onclick="showItemBankInfoAddModal()">
                        <span class="icon-plus icon-white" style="margin-top: 2px"></span> 创建题库
                    </button>
                </div>
                <div style="float: right;">
                    <%--<input type="text" class="form-control" id="searchName" placeholder="请填写题库名称"
                           onkeydown="keyDown(event)" maxlength="20">--%>
                    <input type="text" class="form-control" id="searchName" placeholder="请填写题库名称"
                           onkeyup="keyDown(event)" maxlength="20">
                </div>
                <!-- 题库面板 -->
                <div style="margin-top:74px;overflow-y: scroll;height: 90%;overflow-x: scroll">
                    <div id="itemBankInfo">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 新增题库分类模态框 -->
<div class="modal fade hide" id="categoryAddModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                新增题库分类
            </div>
            <div class="modal-body" style="padding:20px 20px;">
                <label class="moda-label"><span style="color:red;">*</span>分类名称</label>
                <input type="text" id="categoryName" class="form-control"
                       style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="25"
                       placeholder="中文、英文、数字及#">
            </div>
            <!--警告框-->
            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;" id="addCategoryWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px">取消</button>
                <button class="sure" onclick="addCategory();">确定</button>
            </div>
        </div>
    </div>
</div>
<!-- 编辑题库分类模态框 -->
<div class="modal fade hide" id="categoryUpdateModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">

                </button>
                编辑题库分类
            </div>
            <div class="modal-body" style="padding:20px 20px;">
                <label class="moda-label"><span style="color:red;">*</span>分类名称</label>
                <input type="text" id="categoryNameUpdate" class="form-control"
                       style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="25"
                       placeholder="中文、英文、数字及#">
            </div>
            <!--警告框-->
            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;"
                 id="updateCategoryWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px">取消</button>
                <button class="sure" onclick="updateCategory();">确定</button>
            </div>
        </div>
    </div>
</div>
<!-- 新增题库信息模态框 -->
<div class="modal fade hide" id="itemBankInfoAddModal" style="top:34%;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">

                </button>
                新增题库信息
            </div>
            <div class="modal-body" style="padding:20px 20px 20px 15px;height: 258px;">
                <div class="form-group" style="float: left;">
                    <label class="moda-label"><span style="color:red;">*</span>分类名称</label>
                    <select name="itemBankCategorySelect" id="itemBankCategorySelect" class="form-control"
                            style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;"
                            placeholder="请选择分类">
                    </select>
                </div>
                <div class="form-group" style="float: left;margin-top: 20px;">
                    <label class="moda-label"><span style="color:red;">*</span>题库名称</label>
                    <input type="text" id="itemBankName" class="form-control"
                           style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="20"
                           placeholder="中文、英文、数字及下划线">
                </div>
                <div class="form-group" style="float: left;margin-top: 20px;">
                    <label class="moda-label">题库描述</label>
                    <textarea class="form-control"
                              style="width: 280px;height:150px;margin-left: 30px;margin-bottom: 0px;"
                              id="itemBankDescription" rows="4"
                              placeholder="请填写题库描述" maxlength="200"></textarea>
                </div>
            </div>
            <!--警告框-->
            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;" id="addItemBankWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px">取消</button>
                <button class="sure" onclick="addItemBank();">确定</button>
            </div>
        </div>
    </div>
</div>
<!-- 编辑题库信息模态框 -->
<div class="modal fade hide" id="itemBankInfoUpdateModal" style="top:34%;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                编辑题库信息
            </div>
            <div class="modal-body" style="padding:20px 20px 20px 15px;height: 258px;">
                <div class="form-group" style="height: 34px;float: left;">
                    <label class="moda-label"><span style="color:red;">*</span>分类名称</label>
                    <select name="itemBankCategorySelect" id="itemBankCategoryUpdateSelect" class="form-control"
                            style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;"
                            placeholder="请选择分类">
                    </select>
                </div>
                <div class="form-group" style="height: 34px;float: left;margin-top: 20px;">
                    <label class="moda-label"><span style="color:red;">*</span>题库名称</label>
                    <input type="text" id="itemBankNameUpdate" class="form-control"
                           style="width: 280px;margin-left: 30px;margin-bottom: 0px;height: 34px;" maxlength="20"
                           placeholder="中文、英文、数字及下划线">
                </div>
                <div class="form-group" style="height: 34px;float: left;margin-top: 20px;">
                    <label class="moda-label">题库描述</label>
                    <textarea class="form-control"
                              style="width: 280px;height:150px;margin-left: 30px;margin-bottom: 0px;"
                              id="itemBankDescriptionUpdate" rows="4"
                              placeholder="请填写题库描述" maxlength="200"></textarea>
                </div>
            </div>
            <!--警告框-->
            <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;"
                 id="updateItemBankWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px">取消</button>
                <button class="sure" onclick="updateItemBankInfo();">确定</button>
            </div>
        </div>
    </div>
</div>
<!-- 添加题目模态框 -->
<div class="modal fade hide" id="itemAddModal"
     style="position: absolute;left: 700px;height: 854px;width: 680px;top:50%;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                添加题目
            </div>
            <div class="modalBody" id="modalBody"
                 style="height: 700px;overflow-y: scroll;padding: 20px 20px 20px 20px;">
                <label class=" itemType"
                       style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right;"><span
                        style="color:red;">*</span> 题型</label>
                <select id="itemTypeSelect" class="selectItem"
                        onchange="changeType(this.options[this.options.selectedIndex].value)">
                    <option value="1">单选题</option>
                    <option value="2">多选题</option>
                    <option value="3">判断题</option>
                    <%--<option value="4">简答题</option>--%>
                    <option value="5">填空题</option>
                </select><br>
                <label class="itemTitleLabel"
                       style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right"><span
                        style="color:red;">*</span> 题干</label>
                <textarea id="itemTitle" class="itemTitle" rows="3" placeholder="请填写题干内容" maxlength="400"></textarea><br>

                <label class="itemCodeLabel"
                       style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right">代码</label>
                <textarea id="itemCode" class="itemCode" rows="3" placeholder="请填写题目代码内容" maxlength="400"></textarea><br>
                <!-- 添加题目模态框中选项说明区域 -->
                <label class="col-sm-1 control-label labelObj"
                       style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;margin-bottom: 0px;text-align: right"><span
                        style="color:red;">*</span> 选项</label>
                <span class="control-label labelObj" style="color:orange;"><span id="optionTip" style="font-weight: 700;">正确选项个数为1</span></span>
                <!-- 添加题目模态框中添加选项区域 -->
                <div id="optionDiv"></div>
                <!--警告框-->
                <div class="alert alert-block hide" style="color: #ff6c60;margin: 0;width: 89%;"
                     id="addOptionWarn"></div>
                <div style="margin-top: 20px;" id='addOptionBtnDiv'>
                    <button class="sure" id="addOptionBtn" style="width:520px;margin-left: 70px;"
                            onclick="addOption(1);">
                        <img src="../images/add.png" style="margin-bottom: 4px;"/>&nbsp;添加一个选项</span>
                    </button>
                </div>
                <!-- 添加题目模态框中题目解析说明区域 -->
                <label class="itemExplain"
                       style="font-family: '微软雅黑';font-weight: bold;display: inline-block;width: 40px;margin-right:26px;text-align: right">解析</label>
                <span class="labelDetail" style="color:orange;font-weight: 700;">考生/学员查答案时可显示该信息，达到学习的目的</span>
                <textarea class="explainArea" id="answerSolution" rows="3" placeholder="请填写解析内容" maxlength="500"></textarea>
            </div>
            <!--警告框-->
            <div class="alert alert-block hide"
                 style="color: #ff6c60;margin: 0;width: 66%;float: left;position: absolute" id="addItemWarn"></div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px"
                        onclick="hideAlert('addItemWarn')">取消
                </button>
                <button class="sure" onclick="addItemInfo()">确定</button>
            </div>
        </div>
    </div>
</div>
<!-- 弹出框模态框 -->
<div class="modal fade dialog hide" id="warnModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                提示框
            </div>
            <div class="modal-body" style="padding:20px 20px;">
                <span id="warnTip"></span>
            </div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px">关闭</button>
            </div>
        </div>
    </div>
</div>
<!-- 弹出框确认模态框 -->
<div class="modal fade dialog hide" id="sureModal" style="top:250px;"><!-- fade是设置淡入淡出效果 -->
    <div class="modal-dialog category">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                提示框
            </div>
            <div class="modal-body" style="padding:20px 20px;">
                <span id="sureTip"></span>
            </div>
            <div class="modal-footer">
                <button class="cancel" data-dismiss="modal" style="width:70px;margin-right: 0px">取消</button>
                <button class="sure" id="btnExecute" data-dismiss="modal" style="width: 70px;">确定</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog fade hide" id="okModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top:250px;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="padding: 0px 15px;border-top-radius: 4px;">
                <button type="button" class="closeTable" data-dismiss="modal" aria-hidden="true">
                </button>
                <span id="okModalLabel">提示框</span>
            </div>
            <div class="modal-body" id="okModalMessage"  style="height:45px;">
                在这里添加一些文本
            </div>
            <div class="modal-footer">
                <button type="button" class="btcancle" style="width:70px;height:30px;background-color: #b0b5b9"data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
</html>
