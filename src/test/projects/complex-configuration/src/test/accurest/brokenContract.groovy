io.codearte.accurest.dsl.GroovyDsl.make {
				request {
					method 'PUT'
					url '/fraudcheck'
					body("""
						{
						"clientPesel":"${value(client(regex('[0-9]{10}')), server('1234567890'))}",
						"loanAmount":123.123
						}
					"""
					)
					headers {
						header('Content-Type', 'application/vnd.fraud.v1+json')
					}

				}
			response {
				status 999
				body(
						fraudCheckStatus: "OK",
						rejectionReason: $(client(null), server(execute('assertThatRejectionReasonIsNull($it)')))
				)
				headers {
					 header('Content-Type': 'application/vnd.fraud.v1+json')
				}
			}

}
