(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderObjectDeleteController',OrderObjectDeleteController);

    OrderObjectDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderObject'];

    function OrderObjectDeleteController($uibModalInstance, entity, OrderObject) {
        var vm = this;

        vm.orderObject = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderObject.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
