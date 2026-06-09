import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";
export default function MccWiseInterest() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [mcccode, setMcccode] = useState("");
  const [mccdata, setMccdata] = useState([]);
  const [interestRate, setInterestRate] = useState("");
  const [gracePeriod, setGracePeriod] = useState("");
  const [payment, setPayment] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  let userid = localStorage.getItem("userName");
  let participantID = sessionStorage.getItem("Participantid");
  const onlyMccCode = mcccode.split("-");
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
  // Dropdown Account Type
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
        // swal(response.data.message);
      } else {
        // handleShowSuccess(response.data.message);
      }
    } catch (error) {
      // handleShowSuccess(error);
    }
  };

  // Dropdown Mcc Code type
  useEffect(() => {
    Mcccode();
  }, []);

  const Mcccode = async () => {
    try {
      const response = await amsApi.post(
        `mcc_code/getAlMccCode`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setMccdata(response.data.mccListData);
        //  swal(response.data.message);
      } else {
        // handleShowSuccess(response.data.message);
      }
    } catch (error) {
      // handleShowSuccess(error);
    }
  };
  // Save the Mcc interest
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      accounttype === "" ||
      accounttype.length === 0 ||
      mcccode === "" ||
      interestRate.length === "" ||
      gracePeriod === "" ||
      payment === ""
    ) {
      setError(true);
    } else if (!userid || !participantID) {
      swal("Please check your userId and participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `mcc-wise-interest/add`,
          {
            strAccountType: accounttype,
            strMccCode: onlyMccCode[0],
            strInterestRate: interestRate,
            strGracePeriod: gracePeriod,
            strPaymentReceivedWithinDays: payment,
            strCreatedBy: userid,
            strParticipantID: participantID,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.status === 200) {
          handleShowSuccess("Added succefully Mcc Wise Interest");
          setError("");
          setAccountType("");
          setMcccode("");
          setInterestRate("");
          setGracePeriod("");
          setPayment("");
        } else {
          handleShowSuccess(response.data.message);
          //  showError("Added succefully Mcc Wise Interest");
        }
      } catch (error) {
        handleShowSuccess(error.response.data.message);
      }
    }
  };
  const handleClick = () => {
    setAccountType("");
    setMcccode("");
    setInterestRate("");
    setGracePeriod("");
    setPayment("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xl text-black :text-2xl  leading-normal ">
                Mcc Wise Interest
              </p>
            </div>
          </div>
          <div className=" md:col-span-2 lg:col-span-1  ">
            <form className="" onSubmit={handleSubmit}>
              <div className="overflow-hidden shadow ">
                <div className=" px-4  sm:p-2 bg-white  ">
                  <div className="grid gap-2  md:grid-cols-1  px-4 ">
                    <div className="flex">
                      <label
                        htmlFor="GL Account Type"
                        className="block w-auto px-3 py-1 mr-28  text-sm font-normal text-gray-700 bg-white  "
                      >
                        Account Type{""}
                        <span className="text-red-600 px-2 ml-3">*</span>
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
                          <option>
                            {data.strAccountType}-{data.strDescription}
                          </option>
                        ))}
                      </select>
                      {error && accounttype.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please enter Account Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>

                    <div className="flex">
                      <label
                        htmlFor="GL Account Description"
                        className="block w-auto px-3 py-1 mr-32  text-sm font-normal text-gray-700 bg-white  "
                      >
                        MCC Code{" "}
                        <span className="text-red-600 px-2 ml-2">*</span>
                      </label>

                      <select
                        id="mcccode"
                        name="mcccode"
                        disabled={!accounttype}
                        value={mcccode}
                        onChange={(e) => setMcccode(e.target.value)}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {mccdata.map((data) => (
                          <optgroup
                            key={uuidv4()}
                            value={data.strMccCode || ""}
                          >
                            <option>
                              {data.strMccCode || ""} -
                              {data.strMccCodeDesc || ""}
                            </option>
                          </optgroup>
                        ))}
                      </select>
                      {error && mcccode.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please select MCC code
                        </p>
                      ) : (
                        ""
                      )}
                    </div>

                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 mr-32  text-sm font-normal text-gray-700 bg-white  "
                      >
                        Interest Rate{""}
                        <span className="text-red-600 px-2 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="text"
                        name="interestRate"
                        disabled={!mcccode}
                        value={interestRate + "%"}
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
                      />

                      {error && interestRate.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please Interest Rate
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 mr-20  text-sm font-normal text-gray-700 bg-white  "
                      >
                        Grace Period In Days{""}
                        <span className="text-red-600 px-2  ">*</span>
                      </label>
                      <input
                        type="text"
                        id="gracePeriod"
                        disabled={!interestRate}
                        value={gracePeriod}
                        // onChange={(e) => setGracePeriod(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setGracePeriod(value);

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
                          if (gracePeriod.length === 0) {
                            setError("Please Enter Grace Period!");
                            setErrorMessage("");
                          }
                        }}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Grace Period In Days"
                      />
                      {errorMessage ? (
                        <p className="text-red-500 text-sm font-medium">
                          {errorMessage}
                        </p>
                      ) : error && gracePeriod.length <= 0 ? (
                        <p className="text-red-500 text-sm font-medium">
                          Please Enter Grace Period!
                        </p>
                      ) : null}
                      {/* {error && gracePeriod.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Please Grace Period
                        </p>
                      ) : (
                        ""
                      )} */}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-2 py-1  text-sm font-normal text-gray-700 bg-white  "
                      >
                        Payment To Be Received Within Days{""}
                        <span className="text-red-600 px-1">*</span>
                      </label>
                      <input
                        type="text"
                        id="payment"
                        disabled={!gracePeriod}
                        value={payment}
                        // onChange={(e) => setPayment(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setPayment(value);

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
                          if (payment.length === 0) {
                            setError("Enter Payment To Be Received!");
                            setErrorMessage1("");
                          }
                        }}
                        className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Payment To Be Received Within Days"
                      />
                      {errorMessage1 ? (
                        <p className="text-red-500 text-sm font-medium">
                          {errorMessage1}
                        </p>
                      ) : error && payment.length <= 0 ? (
                        <p className="text-red-500 text-sm font-medium">
                          Please Enter Payment To Be Received!
                        </p>
                      ) : null}
                      {/* {error && payment.length <= 0 ? (
                        <p className="text-red-500   text-sm font-medium">
                          Required!
                        </p>
                      ) : (
                        ""
                      )} */}
                    </div>
                  </div>
                </div>
                <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
                  <button
                    title="Click Submit Button"
                    type="submit"
                    data-modal-toggle="defaultModal"
                    className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-3 mx-2  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                  >
                    Submit
                  </button>
                  <button
                    title="Clear Data"
                    type="button"
                    onClick={handleClick}
                    className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-3 mx-2 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                  >
                    Clear
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>

      {/* Your existing code here */}

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
