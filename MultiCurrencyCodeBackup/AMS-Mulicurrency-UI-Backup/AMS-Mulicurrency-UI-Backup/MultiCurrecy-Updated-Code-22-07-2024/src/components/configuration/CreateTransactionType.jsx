import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateTxntype() {
  const [typeid, setTxnid] = useState("");
  const [typekey, setTypekey] = useState("");
  const [typedescription, setTypeDescription] = useState("");
  const [processingcode, setProcessingcode] = useState("");
  const [strGLAccountType, setstrGLAccountType] = useState("");
  const [alldata2, setalldata2] = useState([]);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [isZeroAmountAllow, setisZeroAmountAllow] = useState(""); 
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [isPointAccount, setisPointAccount] = useState("");
  const [interestRate, setinterestRate] = useState("");
  const [immediate_interest, setimmediate_interest] = useState("");
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
    gLAccountCreationlist();
  }, []);

  const gLAccountCreationlist = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/getGlAccTypeAccNumberList`,
        {
          strThirdPartyAllow: "Y",
        }
      );

      if (response.data.code === "S0000") {
        setalldata2(response.data.gLAccountCreationlist);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      typeid === "" ||
      typeid.length === 0 ||
      typekey === "" ||
      typekey.length === 0 ||
      typedescription === "" ||
      typedescription.length === 0 ||
      processingcode === "" ||
      processingcode.length === 0
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `transaction_type_master/saveTxnTypeCreationdata`,
          {
            strTxnTypeId: typeid,
            strTxnTypeDescrp: typedescription,
            strTxnTypeKeyWord: typekey,
            strGLAccountType: strGLAccountType,
            strProcessingCode: processingcode,
            strParticipantId: participantID,
            isZeroAmountAllow: isZeroAmountAllow,
            interestRate: interestRate,
            immediate_interest: immediate_interest,
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setProcessingcode("");
    setTypeDescription("");
    setisZeroAmountAllow("");
    setTypekey("");
    setstrGLAccountType();
    setTxnid("");
    setError("");
    setstrGLAccountType("");
    setisPointAccount("");
    setinterestRate("");
    setimmediate_interest("");
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-6 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Transaction Type Configuration
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1 sm:rounded-md bg-white">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4  sm:p-2 bg-white">
                <div className="grid gap-2  md:grid-cols-1 lg:grid-cols-1 px-2 ">
                  <div className="flex ">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-14
                      py-1 text-sm font-normal text-gray-700 bg-white"
                    >
                      Transaction Type Id{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="Transaction Type Id"
                      value={typeid}
                      // onChange={(e) => setTxnid(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setTxnid(value);

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
                        if (typeid.length === 0) {
                          setError("Please enter Transaction Type Id!");
                          setErrorMessage1("");
                        }
                      }}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Transaction Type Id"
                      autoComplete="off"
                    />
                    {errorMessage1 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage1}
                      </p>
                    ) : error && typeid.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please enter Transaction Type Id!
                      </p>
                    ) : null}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-3 py-1 text-sm font-normal text-gray-700 bg-white"
                    >
                      Transaction Type Keyword{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="type key"
                      value={typekey}
                      disabled={!typeid}
                      onChange={(e) => setTypekey(e.target.value.toUpperCase())}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Transaction Type Keyword"
                      autoComplete="off"
                    />
                    {error && typekey.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Transaction Type Keyword!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-1  py-1 text-sm font-normal text-gray-700 bg-white"
                    >
                      Transaction Type Description
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="txntype Description"
                      value={typedescription}
                      disabled={!typekey}
                      onChange={(e) => setTypeDescription(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Transaction Type Description"
                      autoComplete="off"
                    />
                    {error && typedescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Transaction Description!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 mr-16 text-sm font-normal text-gray-700 bg-white"
                    >
                      Processing Code
                      <span className="text-red-600 px-3">*</span>
                    </label>
                    <input
                      type="text"
                      id="processingcode"
                      value={processingcode}
                      disabled={!typedescription}
                      maxLength={6}
                      minLength={6}
                      // onChange={(e) => setProcessingcode(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setProcessingcode(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage(" Only allow numeric digits");
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (processingcode.length === 0) {
                          setError("Please enter Processing Code!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Processing Code"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && processingcode.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please enter Processing Code!
                      </p>
                    ) : null}
                    {/* {error && processingcode.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please enter Processing Code
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      // className="block text-xs font-semibold text-gray-700"
                      className="block w-auto px-3 mr-16 py-1 text-sm font-normal text-gray-700 bg-white"
                    >
                      Is Point Account{" "}
                      <span className="text-red-600 px-3">*</span>
                    </label>
                    <select
                      name="isPointAccount"
                      id="isPointAccount"
                      value={isPointAccount}
                      disabled={!processingcode}
                      onChange={(e) => setisPointAccount(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="Y">Yes</option>
                      <option value="N">No</option>
                    </select>
                  </div>
                  {isPointAccount === "N" && (
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 mr-14 text-sm font-normal text-gray-700 bg-white"
                      >
                        GL Account Type{""}
                        <span className="text-red-600 px-3 ml-2">*</span>
                      </label>
                      <select
                        name="strGLAccountType"
                        id="strGLAccountType"
                        value={strGLAccountType}
                        onChange={(e) => setstrGLAccountType(e.target.value)}
                        className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option className="capatlize text-md] max-w-[20rem]">
                            {data.strGLAccountType} -{""}
                            {data.strGLAccountNumber || "-"}
                          </option>
                        ))}
                      </select>
                      {/* {error && strGLAccountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select GL Account Type!
                        </p>
                      ) : (
                        ""
                      )} */}
                    </div>
                  )}
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-7 text-sm font-normal text-gray-700 bg-white"
                    >
                      Is Zero Amount allowed
                      <span className="text-red-600 px-2 ">*</span>
                    </label>
                    <select
                      id="isZeroAmountAllow"
                      name="isZeroAmountAllow"
                      value={isZeroAmountAllow}
                      disabled={!isPointAccount}
                      onChange={(e) => setisZeroAmountAllow(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account Generation Automate"
                    >
                      <option value=""> Select</option>
                      <option value="Y"> Yes </option>
                      <option value="N"> No </option>
                    </select>
                    {error && isZeroAmountAllow.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Is Zero Amount Allowed
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-14 text-sm font-normal text-gray-700 bg-white"
                    >
                      Immediate Interest
                      <span className="text-red-600 px-2 ml-1 ">*</span>
                    </label>
                    <select
                      id="interestRate"
                      name="immediate_interest"
                      value={immediate_interest}
                      onChange={(e) => setimmediate_interest(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Immediate Interest"
                    >
                      <option value=""> Select</option>
                      <option value="Y"> Yes </option>
                      <option value="N"> No </option>
                    </select>
                    {error && immediate_interest.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Immediate Interest
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-24  py-1 text-sm font-normal text-gray-700 bg-white"
                    >
                      Interest Rate
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="interestRate "
                      value={interestRate}
                      onChange={(e) => setinterestRate(e.target.value)}
                      className="block w-60 px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Interest Rate"
                      autoComplete="off"
                    />
                    {error && interestRate.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Interest Rate
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>

              <div className="bg-gray-100 px-4 py-2 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border bg-rose-500 hover:bg-gradient-to-bl  py-1.5 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-blue-700   focus:ring-blue-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
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
      {/* </div> */}
    </AppLayout>
  );
}
