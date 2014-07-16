(function($, rf) {
    var DEFAULT_HEIGHT = 200;

    var STYLE_CLASS_ICON    = "pf-confirmdlg-icon";
    var STYLE_CLASS_MESSAGE = "pf-confirmdlg-msg";
    var STYLE_CLASS_OK      = "pf-confirmdlg-ok";
    var STYLE_CLASS_CANCEL  = "pf-confirmdlg-cancel";

    var pf = window.PositiveFaces = window.PositiveFaces || {};
    pf.ui = pf.ui || {};

    pf.ui.ConfirmDialog = function(id, options) {
        $super.constructor.call(this, id, options);
        this.headerDiv = $(rf.getDomElement(id + "_header"));
        this.btnPaneDiv = $(rf.getDomElement(id + '_btn_pane'));
        this.btnPaneDiv.find('.' + STYLE_CLASS_OK).click(function(e) {
            if (pf.confirmSource) {
                var fn = eval('(function(event){'
                    + pf.confirmSource.data('pfconfirmcommand')
                    + '})');
                fn.call(pf.confirmSource, pf.confirmSourceEvent);
                pf.confirmDialog.hide();
                pf.confirmSource = null;
                pf.confirmSourceEvent = null;
            }
            e.preventDefault();
        });
        var contentElmt = $(rf.getDomElement(this.getContentElement()));
        contentElmt.find('.' + STYLE_CLASS_CANCEL).click(function(e) {
            pf.confirmDialog.hide();
            pf.confirmSource = null;
            pf.confirmSourceEvent = null;
            e.preventDefault();
        });
        this.title = $(rf.getDomElement(id + '_header_content'));
        this.message = this.contentDiv.find('span.' + STYLE_CLASS_MESSAGE);
        this.icon = this.contentDiv.find('span.' + STYLE_CLASS_ICON);
        pf.confirmDialog = this;
    };

    rf.ui.PopupPanel.extend(pf.ui.ConfirmDialog);
    var $super = pf.ui.ConfirmDialog.$super;

    $.extend(pf.ui.ConfirmDialog.prototype, (function() {
        return {
            name: "ConfirmDialog",

            destroy: function() {
                this.headerDiv = null;
                this.btnPaneDiv = null;
                this.title = null;
                this.message = null;
                this.icon = null;
                pf.confirmDialog = null;
                $super.destroy.call(this);
            },

            doResizeOrMove: function(diff) {
                var vetoes = $super.doResizeOrMove.call(this, diff);
                if (diff.deltaHeight) {
                    var eContentElt = this.getContentElement();
                    var contentHeight = this.getStyle(eContentElt, "height");
                    contentHeight += diff.deltaHeight;
                    var scrollerHeight;
                    var occupiedHeight = this.headerDiv.outerHeight()
                        + this.btnPaneDiv.outerHeight();
                    if (contentHeight >= this.currentMinHeight
                        || this.options.autosized)
                        scrollerHeight = contentHeight - occupiedHeight;
                    else
                        scrollerHeight = this.currentMinHeight - occupiedHeight;
                    if (contentHeight > this.options.maxHeight)
                        scrollerHeight = this.currentMaxHeight - occupiedHeight;
                    this.scrollerDiv.css('height', scrollerHeight + 'px');
                    if (this.eIframe)
                        this.eIframe.css('height', scrollerHeight + 'px');
                }
                return vetoes;
            },

            show: function(event, opts) {
                if (!this.shown) {
                    $super.show.call(this, event, opts);
                    if (this.shown) {
                        var options = {};
                        $.extend(options, this.options);
                        if (opts)
                            $.extend(options, opts);
                        var height = options.height;
                        if (!this.options.autosized && height && height == -1)
                            height = DEFAULT_HEIGHT;
                        if (height && height != -1)
                            this.scrollerDiv.css('height', height
                                - this.headerDiv.outerHeight()
                                - this.btnPaneDiv.outerHeight() + 'px');
                    }
                }
            },

            showMessage: function(msg) {
                if (msg.header)
                    this.title.text(msg.header);
                if (msg.message)
                    this.message.text(msg.message);
                if (msg.icon)
                    this.icon.removeClass()
                        .addClass(STYLE_CLASS_ICON + ' ' + msg.icon);
                this.show();
            }
        }
    })());
})(jQuery, window.RichFaces);
