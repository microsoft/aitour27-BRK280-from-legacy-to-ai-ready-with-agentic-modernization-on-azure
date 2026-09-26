using System;
using System.Runtime.Serialization;

namespace CaldovaDrugPricingService
{
    [DataContract(Name = "CurrencyRateData", Namespace = "http://zava.bank/wcf/currency/")]
    public class DrugPricingRateData
    {
        [DataMember(Order = 1)]
        public string FromCurrency { get; set; }

        [DataMember(Order = 2)]
        public string ToCurrency { get; set; }

        [DataMember(Order = 3)]
        public decimal Rate { get; set; }

        [DataMember(Order = 4)]
        public string Source { get; set; }

        [DataMember(Order = 5)]
        public DateTime EffectiveDate { get; set; }
    }
}
