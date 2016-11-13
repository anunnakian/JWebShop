(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderLineDeleteController',OrderLineDeleteController);

    OrderLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderLine'];

    function OrderLineDeleteController($uibModalInstance, entity, OrderLine) {
        var vm = this;

        vm.orderLine = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
