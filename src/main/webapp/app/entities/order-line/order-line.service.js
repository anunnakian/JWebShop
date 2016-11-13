(function() {
    'use strict';
    angular
        .module('jWebShopApp')
        .factory('OrderLine', OrderLine);

    OrderLine.$inject = ['$resource'];

    function OrderLine ($resource) {
        var resourceUrl =  'api/order-lines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
