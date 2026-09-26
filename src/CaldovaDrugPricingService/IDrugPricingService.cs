using System.ServiceModel;

namespace CaldovaDrugPricingService
{
    [ServiceContract(Name = "IZavaCurrencyService", Namespace = "http://zava.bank/wcf/currency/")]
    public interface IDrugPricingService
    {
        [OperationContract]
        DrugPricingRateData GetRate(string fromCurrency, string toCurrency);

        [OperationContract]
        string[] GetSupportedCurrencies();

        [OperationContract]
        decimal ConvertAmount(decimal amount, string fromCurrency, string toCurrency);
    }
}
