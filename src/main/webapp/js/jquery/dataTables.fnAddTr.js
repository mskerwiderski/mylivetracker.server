$.fn.dataTableExt.oApi.fnAddTr = function ( oSettings, nTr, bRedraw ) {
    if (typeof bRedraw == 'undefined') {
    	bRedraw = true;
    }
    //var nTds = nTr.getElementsByTagName("td");
    var nTds = $(".trackRowCol", nTr);
    if (nTds.length != oSettings.aoColumns.length) {
        alert( 'Warning: not adding new TR - columns and TD elements must match' );
        alert("found nTds: " + nTds.length);
        alert("number of cols: " + oSettings.aoColumns.length);
        return;
    }
    var aData = [];
    for (var i=0; i<nTds.length; i++) {
        aData.push(nTds[i].innerHTML);
    }
    var iIndex = this.oApi._fnAddData(oSettings, aData);
    nTr._DT_RowIndex = iIndex;
    oSettings.aoData[iIndex].nTr = nTr;
    oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
    if (bRedraw) {
        this.oApi._fnReDraw(oSettings);
    }
};
