# financial-service
An implementation for financial solution.

1. Given API is timing out as I am working behind proxy hence its response have been mocked to proceed with the task.
2. API call 'http://localhost:8080/norway/finance/info/BMG9156K1018?fromDate=&toDate=' will give information of one ISIN with multiple events.
3. API call 'http://localhost:8080/norway/finance/info/BMG9156K1018?fromDate=2022-05-01&toDate=2022-05-03' will give information of one ISIN with multiple events.
4. Events in between of input from & to date will be returned to the response on the basis of event date. 