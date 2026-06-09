import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function RevolvingCredit() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [Billingcycle, setBillingcycle] = useState("");
  const [Graceperiod, setGracePeriod] = useState("");
  const [minamount, setMinAmount] = useState("");
  // const [interestrate, setInterestRate] = useState("");
  // const [penultyrate, setPenultyRate] = useState("");
  const [latepayment, setLatePayment] = useState("");
  const [immediateCharge, setimmediateCharge] = useState("");
  const [transactionCharge, settransactionCharge] = useState("");
  // const [transactionGstCharge, settransactionGstCharge] = useState("");
  const [deliquencydays, setDeliquencyDays] = useState("");
  // const [cashAdvanceIntrest, setcashAdvanceIntrest] = useState(""); 
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const [errorMessage2, setErrorMessage2] = useState("");
  const [errorMessage3, setErrorMessage3] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  useEffect(() => {
    Accounttype();
  }, []);
  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getYCrditAccounType`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error);
    }
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      Billingcycle === "" ||
      Billingcycle.length === 0 ||
      accounttype === "" ||
      accounttype.length === 0 ||
      Graceperiod === "" ||
      minamount === "" ||
      // interestrate === "" ||
      // penultyrate === "" ||
      // latepayment === "" ||
      deliquencydays === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `revolvingCreditCard/validateRevolvngCreditCard`,
          {
            strAccountType: accounttype,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code !== "S0000") {
          const response1 = await amsApi.post(
            `revolvingCreditCard/addRevolvngCreditCard`,
            {
              strParticipantID: participantID,
              strAccountType: accounttype,
              strBillingCycleDate: Billingcycle,
              strGracePeriodInDays: Graceperiod,
              strMadRate: minamount,
              // strInterestRate: interestrate,
              // strPenultyInterestRate: penultyrate,
              strLatePaymentFees: latepayment,
              strDeliquencyDays: deliquencydays,
              immediateCharge: immediateCharge,
              transactionCharge: transactionCharge,
              // transactionGstCharge: transactionGstCharge,
              // cashAdvanceIntrest: cashAdvanceIntrest,
            }
          );
          if (response1.data.code === "S0000") {
            handleShowSuccess(response1.data.message);
            handleClick();
            setError("");
          } else {
            handleShowError(response1.data.message);
          }
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };
  const handleClick = () => {
    setAccountType("");
    setBillingcycle("");
    setGracePeriod("");
    setMinAmount("");
    setDeliquencyDays("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-1 ">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Revolving Credit Card Calculation
              </p>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1 max-h-[24rem] ">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow ">
              <div className=" px-4 sm:p-1 bg-white  ">
                <div className="grid gap-2  lg:grid-cols-1 px-4">
                  <div className="flex">
                    <label
                      htmlFor="GL Account Type"
                      className="block w-auto px-3 py-1 mr-28 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Account Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={accounttype}
                      onChange={(e) => setAccountType(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {type.map((data) => (
                        <option value={data.strAccountType}>
                          {data.strAccountType || "-"} -
                          {data.strDescription || "-"}
                        </option>
                      ))}
                    </select>
                    {error && accounttype.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter Account Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="GL Account Description"
                      className="block w-auto px-3 py-1 mr-20 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Billing Cycle Date{""}
                      <span className="text-red-600 px-3">*</span>
                    </label>

                    <input
                      type="text"
                      minLength={1}
                      maxLength={3}
                      value={Billingcycle}
                      onChange={(e) => {
                        const value = e.target.value;
                        setBillingcycle(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (Billingcycle.length === 0) {
                          setErrorMessage("");
                        }
                      }}
                      placeholder="Enter Billingcycle"
                      autoComplete="off"
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && Billingcycle.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Billing Cycle Date!
                      </p>
                    ) : null}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-16 ml-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Grace Period In Days{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="text"
                      minLength={1}
                      maxLength={3}
                      value={Graceperiod}
                      // onChange={(e) => setGracePeriod(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setGracePeriod(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage1(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage1("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (Graceperiod.length === 0) {
                          // setError("Please Enter Grace Period!");
                          setErrorMessage1("");
                        }
                      }}
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Grace Period In Days"
                      autoComplete="off"
                    />
                    {errorMessage1 ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage1}
                      </p>
                    ) : error && Graceperiod.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Grace Period!
                      </p>
                    ) : null}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Minimum Amount Due on TAD{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="text"
                      value={minamount + "%"}
                      onChange={(e) => {
                        const inputValue = e.target.value.replace("%", "");

                        if (
                          inputValue === "" ||
                          (parseFloat(inputValue) >= 0 &&
                            parseFloat(inputValue) <= 100)
                        ) {
                          setMinAmount(inputValue);
                        }
                      }}
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Minimum Amount Due on TAD(%)"
                      autoComplete="off"
                    />
                    {error && minamount.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter MAD on TAD!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {/* <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 mr-28 ml-1 text-sm font-normal text-gray-700 bg-white  "
                      >
                        Interest Rate{""}
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <input
                        type="percentage"
                        id="percentage"
                        value={interestrate + "%"}
                        onChange={(e) => {
                          const inputValue = e.target.value.replace("%", "");

                          if (
                            inputValue === "" ||
                            (parseFloat(inputValue) >= 0 &&
                              parseFloat(inputValue) <= 100)
                          ) {
                            setInterestRate(inputValue);
                          }
                        }}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Interest Rate (%)"
                        autoComplete="off"
                      />
                      {error && interestrate.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please Enter Interest Rate!
                        </p>
                      ) : (
                        ""
                      )}
                    </div> */}
                  {/* <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 mr-16 ml-1 text-sm font-normal text-gray-700 bg-white  "
                      >
                        Penalty Interest Rate{""}
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="text"
                        value={penultyrate + "%"}
                        onChange={(e) => {
                          const inputValue = e.target.value.replace("%", "");

                          if (
                            inputValue === "" ||
                            (parseFloat(inputValue) >= 0 &&
                              parseFloat(inputValue) <= 100)
                          ) {
                            setPenultyRate(inputValue);
                          }
                        }}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Penulty Interest Rate "
                        autoComplete="off"
                      />
                      {error && penultyrate.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please Enter Penulty Interest Rate!
                        </p>
                      ) : (
                        ""
                      )}
                    </div> */}
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-20 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Late Payment Fees{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="text"
                      maxLength={4}
                      value={latepayment}
                      // onChange={(e) => setLatePayment(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setLatePayment(value);
                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage2(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage2("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (latepayment.length === 0) {
                          setError("Please Enter Late Payment Fees!");
                          setErrorMessage2("");
                        }
                      }}
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Late Payment Fees"
                      autoComplete="off"
                    />
                    {errorMessage2 ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage2}
                      </p>
                    ) : error && latepayment.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Late Payment Fees!
                      </p>
                    ) : null}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-10 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Immediate Charge Type
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="immediateCharge"
                      name="immediateCharge"
                      value={immediateCharge}
                      onChange={(e) => setimmediateCharge(e.target.value)}
                      // disabled={!linkedcode}
                      className="block w-60 px-3 py-1 sm:text-xs ml-2 font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="Y">YES</option>
                      <option value="N">NO</option>
                    </select>
                    {error && immediateCharge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please select Immediate Charge Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {immediateCharge === "Y" && (
                    <>
                      <div className="flex">
                        <label
                          htmlFor="text"
                          className="block w-auto px-3 py-1 mr-24 text-sm font-normal text-gray-700 bg-white  "
                        >
                          Transaction Charge
                          <span className="text-red-600  ">*</span>
                        </label>
                        <input
                          type="text"
                          id="transactionCharge"
                          value={transactionCharge}
                          onChange={(e) =>
                            settransactionCharge(e.target.value.toUpperCase())
                          }
                          className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Transaction Charge"
                          autoComplete="off"
                        />
                        {error && transactionCharge.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Enter Transaction Charge
                          </p>
                        ) : (
                          ""
                        )}
                      </div>

                      {/* <div className="flex">
                          <label
                            htmlFor="text"
                            className="block w-auto px-3 py-1 mr-12 ml-1 text-sm font-normal text-gray-700 bg-white  "
                          >
                            Transaction GST Charge{""}
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="text"
                            id="transactionGstCharge"
                            value={transactionGstCharge + "%"}
                            onChange={(e) => {
                              const inputValue = e.target.value.replace(
                                "%",
                                ""
                              );
                              if (
                                inputValue === "" ||
                                (parseFloat(inputValue) >= 0 &&
                                  parseFloat(inputValue) <= 100)
                              ) {
                                settransactionGstCharge(inputValue);
                              }
                            }}
                            className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                            placeholder="Enter Transaction GST Charge"
                            autoComplete="off"
                          />
                          {error && transactionGstCharge.length <= 0 ? (
                            <p className="text-red-500   text-sm font-medium">
                              Please Enter Transaction GST Charge!
                            </p>
                          ) : (
                            ""
                          )}
                      </div> */}

                      {/* <div className="flex">
                          <label
                            htmlFor="text"
                            className="block w-auto px-3 py-1 mr-12 ml-1 text-sm font-normal text-gray-700 bg-white  "
                          >
                            Cash Advanced Interest{""}
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="text"
                            id="cashAdvanceIntrest"
                            value={cashAdvanceIntrest + "%"}
                            onChange={(e) => {
                              const inputValue = e.target.value.replace(
                                "%",
                                ""
                              );
                              if (
                                inputValue === "" ||
                                (parseFloat(inputValue) >= 0 &&
                                  parseFloat(inputValue) <= 100)
                              ) {
                                setcashAdvanceIntrest(inputValue);
                              }
                            }}
                            className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                            placeholder="Enter Cash Advanced Interest"
                            autoComplete="off"
                          />
                          {error && cashAdvanceIntrest.length <= 0 ? (
                            <p className="text-red-500   text-sm font-medium">
                              Please Enter Cash Advanced Interest!
                            </p>
                          ) : (
                            ""
                          )}
                        </div> */}
                    </>
                  )}
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-20 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Delinquency Days{""}
                      <span className="text-red-600 px-2 ml-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="text"
                      value={deliquencydays}
                      // onChange={(e) => setDeliquencyDays(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setDeliquencyDays(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage3(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage3("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (deliquencydays.length === 0) {
                          // setError("Please Enter Deliquency Days!");
                          setErrorMessage3("");
                        }
                      }}
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Delinquency Days"
                      autoComplete="off"
                    />
                    {errorMessage3 ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage3}
                      </p>
                    ) : error && deliquencydays.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Deliquency Days!
                      </p>
                    ) : null}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4  text-right ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center  bg-blue-500 hover:bg-blue-700  focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 border-blue-700 py-1.5 px-3 mx-2 my-2 text-sm font-semibold text-white rounded "
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center  bg-rose-500 hover:bg-rose-700 b  focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 border-rose-700 py-1.5 px-3 mx-2 my-2  text-sm font-semibold text-white rounded "
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
