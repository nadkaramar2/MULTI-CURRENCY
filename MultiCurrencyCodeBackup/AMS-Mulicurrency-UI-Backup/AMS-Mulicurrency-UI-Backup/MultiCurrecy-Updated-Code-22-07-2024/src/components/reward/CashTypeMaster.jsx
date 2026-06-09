import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CashTypeMaster() {
  const [alldata2, setalldata2] = useState([]);
  const [cashbackType, setcashbackType] = useState("");
  const [cashbackDescr, setcashbackDescr] = useState("");
  const [glAccountType, setglAccountType] = useState("");
  const [glAccountNumber, setglAccountNumber] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  let useid = localStorage.getItem("userName");
  //d
  const [acno, setAcno] = useState("");
  console.log("acno:" + acno);
  const [actype, setActype] = useState("");
  console.log("actype:" + actype);
  // const str = glAccountType;
  // const [accountType, accountNo] = str.split(" ");
  // let formateAccountnumber = accountNo.split(" ");
  //console.log(str.accountNo.replace(/[^a-zA-Z ]/g, ""));
  // let formt = str;
  // if (formt[0] === "-") {
  //   formt = formt.slice(1);
  // }
  // console.log("vv" + formt);
  // "MPA_GL" console.log(accountNo); // "-999010000001"

  // console.log(formateAccountnumber);

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
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  const handleSubmit = async (event) => {
    // event.preventDefault();
    if (
      cashbackType === "" ||
      cashbackType.length === 0 ||
      cashbackDescr === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          // `cashback/NGN/TypeCreation`,
          `cashback/NGN/TypeCreation`,
          {
            participantId: participantID,
            cashbackType: cashbackType,
            cashbackDescr: cashbackDescr,
            glAccountType: actype,
            glAccountNumber: acno,
            createdBy: useid,
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
    setcashbackType("");
    setcashbackDescr("");
    setglAccountType("");
    setglAccountNumber("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base sm:text-xm text-black :text-2xl  leading-normal ">
                CashBack Type Master
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 bg-white">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white  ">
                <div className="grid gap-4 mb-2 px-8  md:grid-cols-3 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      CashBack Type<span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="type"
                      value={cashbackType}
                      onChange={(e) => {
                        const value = e.target.value.toUpperCase();
                        setcashbackType(value);
                        const regex = /^[a-zA-Z]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage("Input can't be Numeric");
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (cashbackType.length === 0) {
                          setError("Enter CashBack Type");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter CashBack Type   "
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && cashbackType.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter CashBack Type
                      </p>
                    ) : null}
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      CashBack Description
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      id="description"
                      value={cashbackDescr}
                      onChange={(e) => setcashbackDescr(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter CashBack Description"
                    />
                    {error && cashbackDescr.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter CashBack Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      GL Account Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="glAccountType"
                      id="glAccountType"
                      onChange={(e) => {
                        const selectedOption = e.target.value;
                        const [strGLAccountType, strGLAccountNumber] =
                          selectedOption.split(" - ");
                        setActype(strGLAccountType);
                        setAcno(strGLAccountNumber);
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata2.map((data) => (
                        <option className="capitalize text-sm" key={data.id}>
                          {`${data.strGLAccountType} - ${
                            data.strGLAccountNumber || "-"
                          }`}
                        </option>
                      ))}
                    </select>
                    {error && glAccountType.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Select GL Account Type!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4  text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-3 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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
    </AppLayout>
  );
}
