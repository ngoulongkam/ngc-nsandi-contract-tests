
# ngc-nsandi-contract-tests

 [ ![Download](https://api.bintray.com/packages/hmrc/releases/ngc-nsandi-contract-tests/images/download.svg) ](https://bintray.com/hmrc/releases/ngc-nsandi-contract-tests/_latestVersion)

Contract tests for the contracts that #team-nextgenconsumer's services expect of National Savings and Investments' (NS&I's) APIs.

Note that these tests do not connect directly to NS&I, they connect to help-to-save-proxy which in turn connects to NS&I. help-to-save-proxy handles authentication to NS&I using certificates and HTTP basic auth.

## Running locally:

```
sm --start NGC_HELP_TO_SAVE_CONTRACT_TESTS -f
sbt test
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
