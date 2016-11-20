(function() {
    'use strict';
    angular
        .module('jWebShopApp')
        .factory('ProductCategory', ProductCategory);

    ProductCategory.$inject = ['$resource'];

    function ProductCategory ($resource) {
        var resourceUrl =  'api/products/category/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
