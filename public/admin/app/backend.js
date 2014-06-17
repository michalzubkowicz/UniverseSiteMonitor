define(function(require){
    return {
        getLabels:function(){
            return $.ajax({
                url: '/admin/label/',
                type: 'GET'
            });
        },
        getResponses:function(skip){
            return $.ajax({
                url: '/admin/response/'+skip,
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