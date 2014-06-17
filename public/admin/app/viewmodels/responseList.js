
define(['knockout','backend','models/Response', 'durandal/app'], function (ko, backend,Response,app) {
    return {
        self:this,
        responses: ko.observableArray([]),
        currentSkip: ko.observable(1),
        noNext: ko.observable(true),
        noPrev: ko.observable(false),
        activate:function(){
            var that = this;
            that.currentSkip.subscribe(function() {
                backend.getResponses(that.currentSkip()).then(function(results){
                    that.responses(ko.utils.arrayMap(results, function(item) {
                        var l = new Response(item._id);
                        l.fromJS(item);
                        return l;
                    }));

                    if(results.length==0) {
                        that.noNext(true);
                    } else {
                        that.noNext(false);
                    }

                    if(that.currentSkip()==0) that.noPrev(true);
                    else that.noPrev(false);
                });
            });

            //default skip
            that.currentSkip(0);
            return "";
        },
        nextPage: function() {
            this.currentSkip(this.currentSkip()+10);
        },
        prevPage: function() {
            var cs = this.currentSkip()-10;
            if(cs<0) cs=0;
            this.currentSkip(cs);

        }
    };
});