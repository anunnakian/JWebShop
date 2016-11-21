(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('ProductController', ProductController);

    ProductController.$inject = ['$scope', '$state', 'DataUtils', 'Product', 'ProductSearch'];

    function ProductController ($scope, $state, DataUtils, Product, ProductSearch) {
        var vm = this;
        
        vm.products = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Product.query(function(result) {
                vm.products = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProductSearch.query({query: vm.searchQuery}, function(result) {
                vm.products = result;
            });
        }    }
})();
