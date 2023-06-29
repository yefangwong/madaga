/*
* DHF公用 js library
* @author wyf
* @date 2023/06/28
* */
var csp = {
     toPostSubmit: function(url, winObj) {
        if (winObj==null){
            winObj = window;
        }
        try {
            var exp = /\?|&|=|#/gi;
            var rUrl = url.replace(exp, '&');
            var listArr = rUrl.split('&');
            var sTmp = '';
            sTmp += ('<form name="toPostForm" method="POST">');
            var tmp = '';
            for(var i=1; i<listArr.length; i=i+2){
                tmp += listArr[i] + ' = ' + listArr[i+1] + '\n';
                sTmp += ('<input type="hidden" name="' + listArr[i] + '" value="' +
                    listArr[i+1]+'">');
            }
            //alert(tmp);
            sTmp += ('</form>');
            if(winObj.divToPostFormSubmit != null) {
                winObj.divToPostFormSubmit.innerHTML = sTmp;
            } else {
                winObj.document.write(sTmp);
            }
            winObj.document.toPostForm.action = listArr[0];
            winObj.document.toPostForm.submit();
        } catch (e) {
            winObj.location = url;
        }
        winObj.focus();
    }
};

