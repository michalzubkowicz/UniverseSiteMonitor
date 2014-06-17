define(function(require){
    return {
        getLabels:function(){
            return $.ajax({
                url: '/admin/label/',
                type: 'GET'
            });
        },
        getResponses:function(){
            return $.ajax({
                url: '/admin/response/',
                type: 'GET'
            });
        },
        getServices:function(){
            return $.ajax({
                url: '/admin/service/',
                type: 'GET'
            });
        }
    };
});