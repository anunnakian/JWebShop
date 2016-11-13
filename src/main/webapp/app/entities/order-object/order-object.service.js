(function() {
    'use strict';
    angular
        .module('jWebShopApp')
        .factory('OrderObject', OrderObject);

    OrderObject.$inject = ['$resource'];

    function OrderObject ($resource) {
        var resourceUrl =  'api/order-objects/:id';

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
