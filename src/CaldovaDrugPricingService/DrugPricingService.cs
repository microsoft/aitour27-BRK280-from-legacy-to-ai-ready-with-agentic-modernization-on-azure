using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.ServiceModel;

namespace CaldovaDrugPricingService
{
    public class DrugPricingService : IDrugPricingService
    {
        public DrugPricingRateData GetRate(string fromCurrency, string toCurrency)
        {
            var normalizedFrom = NormalizeCurrency(fromCurrency);
            var normalizedTo = NormalizeCurrency(toCurrency);

            using (var connection = new SqlConnection(DbConfig.GetConnectionString()))
            using (var command = new SqlCommand(@"
SELECT TOP 1 cp.BaseCurrency, cp.QuoteCurrency, er.MidRate, er.Source, er.EffectiveDate
FROM CurrencyPairs cp
INNER JOIN ExchangeRates er ON er.PairID = cp.PairID
WHERE cp.BaseCurrency = @FromCurrency
  AND cp.QuoteCurrency = @ToCurrency
  AND cp.IsActive = 1
  AND (er.ExpiryDate IS NULL OR er.ExpiryDate > GETDATE())
ORDER BY er.EffectiveDate DESC;", connection))
            {
                command.Parameters.AddWithValue("@FromCurrency", normalizedFrom);
                command.Parameters.AddWithValue("@ToCurrency", normalizedTo);

                connection.Open();
                using (var reader = command.ExecuteReader())
                {
                    if (!reader.Read())
                    {
                        throw new FaultException("No exchange rate found for " + normalizedFrom + "/" + normalizedTo + ".");
                    }

                    return new DrugPricingRateData
                    {
                        FromCurrency = reader.GetString(0),
                        ToCurrency = reader.GetString(1),
                        Rate = reader.GetDecimal(2),
                        Source = reader.GetString(3),
                        EffectiveDate = reader.GetDateTime(4)
                    };
                }
            }
        }

        public string[] GetSupportedCurrencies()
        {
            var currencies = new List<string>();

            using (var connection = new SqlConnection(DbConfig.GetConnectionString()))
            using (var command = new SqlCommand(@"
SELECT CurrencyCode
FROM (
    SELECT DISTINCT BaseCurrency AS CurrencyCode FROM CurrencyPairs WHERE IsActive = 1
    UNION
    SELECT DISTINCT QuoteCurrency AS CurrencyCode FROM CurrencyPairs WHERE IsActive = 1
) c
ORDER BY CurrencyCode;", connection))
            {
                connection.Open();
                using (var reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        currencies.Add(reader.GetString(0));
                    }
                }
            }

            return currencies.ToArray();
        }

        public decimal ConvertAmount(decimal amount, string fromCurrency, string toCurrency)
        {
            if (amount < 0)
            {
                throw new FaultException("Amount must be non-negative.");
            }

            var rate = GetRate(fromCurrency, toCurrency);
            return decimal.Round(amount * rate.Rate, 4, MidpointRounding.AwayFromZero);
        }

        private static string NormalizeCurrency(string currency)
        {
            if (string.IsNullOrWhiteSpace(currency) || currency.Trim().Length != 3)
            {
                throw new FaultException("Drug pricing code must be exactly 3 letters.");
            }

            return currency.Trim().ToUpperInvariant();
        }
    }
}
