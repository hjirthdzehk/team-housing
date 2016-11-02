/**
 * Created by VladVin on 02.11.2016.
 */

var MeasurementsHistoryViewModel = function (meterHistory) {
    var self = this;

    self.meterHistoryGroup = ko.observableArray(
        _.map(meterHistory,
            function (meter) {
                return {
                    title: ko.observable(meter.meterTitle),
                    meterReadings: ko.observableArray(_.map(
                        meter.meterReadings,
                        function (meterReading) {
                            return {
                                value: ko.observable(meterReading.value),
                                unit: ko.observable(meter.meterUnitTitle),
                                date: ko.observable(meterReading.date),
                                paid: ko.observable(meterReading.paid ? "Paid" : "Not paid"),
                            }
                        }
                    ))
                }
            }
        ));
};