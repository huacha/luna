<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="row-fluid">

	<!-- start of main -->
    <section id="m-main" class="span10">
	  <div id="__gef_container__" style="padding-left:5px;">
	    <div id="__gef_palette__" style="float:left;width:260px;">
		  <ul class="nav nav-tabs" id="myTab">
            <li class="active"><a href="#operation" data-toggle="tab">操作</a></li>
			<li><a href="#form" data-toggle="tab">字段属性</a></li>
			<li><a href="#prop" data-toggle="tab">表单属性</a></li>
		  </ul> 
		  <div class="tab-content">
			<div class="tab-pane active" id="operation">
			  <div>
				<div class="xf-pallete" title="label">
				  <img src="${ctx}/widgets/xform/images/xform/new_label.png">
				  label
				</div>
				<div class="xf-pallete" title="textfield">
				  <img src="${ctx}/widgets/xform/images/xform/new_input.png">
				  textfield
				</div>
				<div class="xf-pallete" title="password">
				  <img src="${ctx}/widgets/xform/images/xform/new_secret.png">
				  password
				</div>
				<div class="xf-pallete" title="textarea">
				  <img src="${ctx}/widgets/xform/images/xform/new_textarea.png">
				  textarea
				</div>
				<div class="xf-pallete" title="select">
				  <img src="${ctx}/widgets/xform/images/xform/new_select.png">
				  select
				</div>
				<div class="xf-pallete" title="radio">
				  <img src="${ctx}/widgets/xform/images/xform/new_item.png">
				  radio
				</div>
				<div class="xf-pallete" title="checkbox">
				  <img src="${ctx}/widgets/xform/images/xform/new_itemset.png">
				  checkbox
				</div>
				<div class="xf-pallete" title="fileupload">
				  <img src="${ctx}/widgets/xform/images/xform/new_upload.png">
				  fileupload
				</div>
				<div class="xf-pallete" title="datepicker">
				  <img src="${ctx}/widgets/xform/images/xform/new_range.png">
				  datepicker
				</div>
				<div class="xf-pallete" title="userpicker">
				  <img src="${ctx}/widgets/xform/images/xform/userpicker.png">
				  userpicker
				</div>
			  </div>
			</div>
			<div class="tab-pane" id="form">
			  <div class="popover" style="display:block;position:relative;">
				<h3 class="popover-title">属性</h3>
				<div class="popover-content">
				  <div id="xf-form-attribute" class="controls"></div>
				</div>
			  </div>
			</div>
			<div class="tab-pane" id="prop">
			  <div class="popover" style="display:block;position:relative;">
				<h3 class="popover-title">属性</h3>
				<div class="popover-content">
				  <div id="xf-form-attribute" class="controls">
				    <label>
					  表单名称
				      <input id="xFormName" type="text">
					</label>
				    <label>
					  数据库表名
				      <input id="xFormCode" type="text">
                    </label>
				  </div>
				</div>
			  </div>
			</div>
		  </div>
	    </div>

		<div class="__gef_center__">
		<div id="__gef_toolbar__">
		  <div style="width:50px;float:left;">&nbsp;</div>
		  <div class="btn-group">
			<button class="btn" onclick="doSave()">保存</button>
<!--
			<button class="btn" onclick="alert(xform.doExport())">export</button>
			<button class="btn" onclick="doImport()">import</button>
-->
			<button class="btn" onclick="xform.addRow()">增加一行</button>
			<button class="btn" onclick="doChangeMode(this)">切换到合并模式</button>
			<button class="btn" onclick="doMerge()">合并</button>
			<button class="btn" onclick="doSplit()">拆分</button>
		  </div>
		</div>


		<div id="__gef_canvas__" style="overflow:auto;">
		  <div id="xf-center" class="xf-center" unselectable="on">
			<div id="xf-layer-form" class="xf-layer-form">
			  <form id="xf-form" action="#" method="post" class="controls">
			  </form>
			</div>
			<div id="xf-layer-mask" class="xf-layer-mask">
			</div>
		  </div>
		</div>
	  </div>

	  </div>
    </section>
	<!-- end of main -->

    <form:form id="f" commandName="m" method="post" style="display:none;">
	  <input id="__gef_id__" name="id" value="${m.id}">
	  <input id="__gef_name__" name="name" value="${m.name}">
	  <input id="__gef_code__" name="code" value="${m.code}">
	  <textarea id="__gef_content__" name="content">${m.content}</textarea>
	</form:form>
</div>
<es:contentFooter/>
<link href="${ctx}/widgets/xform/styles/xform.css" rel="stylesheet">
<script src="${ctx}/widgets/xform/adaptor.js" type="text/javascript"></script>
<script src="${ctx}/widgets/xform/xform-all.js" type="text/javascript"></script>
