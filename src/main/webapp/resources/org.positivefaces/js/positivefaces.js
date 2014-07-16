(function($, rf) {
    var pf = window.PositiveFaces = window.PositiveFaces || {};

    pf.confirm = function(msg) {
        if (pf.confirmDialog) {
            pf.confirmSource = $("#" + msg.source.replace(/:/g, "\\:"));
            pf.confirmSourceEvent = msg.sourceEvent;
            pf.confirmDialog.showMessage(msg);
        } else
            rf.log.warn('No global confirmation dialog available');
    };
})(jQuery, window.RichFaces);
