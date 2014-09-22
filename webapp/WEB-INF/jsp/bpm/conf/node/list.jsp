<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <es:showMessage/>

    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
                
	        <a href="<es:BackURL/>">
	            <i class="icon-reply"></i>
	            返回
	        </a>
        </div>
    </div>
    
    <h4 class="hr tool"> 流程节点</h4>
    <div class="span7">
		<table id="nodeTable" class="table table-bordered table-hover" >
			<tr>
				<td style="width: 100px; text-align:right" >流程名称:</td>
				<td style="width: 250px">${bpmProcess.name} </td>
				<td style="width: 100px; text-align:right" align="right">描述：</td>
				<td style="width: 250px">${bpmProcess.descn} </td>
			</tr>
		</table>
	</div>
    
    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>

</script>