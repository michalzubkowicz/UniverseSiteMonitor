define(['knockout','backend','models/Label'], function (ko, backend,Label) {
    return {
        label:ko.observable(),
        activate:function(id){
            var l = new Label(id);
            l.Load();
            this.label(l);
        }
    };
});