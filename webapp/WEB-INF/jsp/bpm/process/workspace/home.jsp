<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">
	<es:showMessage/>
	
    <!-- start of main -->
    <section id="m-main" class="span12" style="float:center">
	<c:forEach items="${bpmCategories}" var="bpmCategory">
	      <div class="row-fluid">
	      <div class="page-header">
	        <h3>${bpmCategory.name}</h3>
	      </div>
	
	      <c:forEach items="${bpmCategory.bpmProcesses}" var="bpmProcess">
	        <div class="well span2">
	          <h4>${bpmProcess.name}&nbsp;</h4>
	          <p>${bpmProcess.descn}&nbsp;</p>
	          <div class="btn-group">
	            <a class="btn btn-small" href="${ctx}/xform/process/viewStartForm?bpmProcessId=${bpmProcess.id}"><li class="icon-play"></li>发起</a>
	            <a class="btn btn-small" href="${ctx}/bpm/process/workspace/graphProcessDefinition.do?bpmProcessId=${bpmProcess.id}" target="_blank"><li class="icon-picture"></li>图形</a>
	          </div>
	        </div>
	      </c:forEach>
		  </div>
	</c:forEach>
    </section>
    <!-- end of main -->
    
</div>
<es:contentFooter/>
