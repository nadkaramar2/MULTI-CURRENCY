import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CardAccountLinkage() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [accountno, setAccountno] = useState("");
  const [cardtype, setCardtype] = useState("");
  const [cardno, setCardno] = useState("");
  const [cardtypedata, setCardTypedata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
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
        `accountType/getACTypeListByParticiptWise`,
        {
          strParticipantId: participantID,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
      }
    } catch (error) {}
  };

  // Card type Dropdown
  useEffect(() => {
    Cardtype();
  }, []);

  const Cardtype = async () => {
    try {
      const response = await amsApi.post(
        `card_type/getCardTypedataForLinkage`,
        {
          strParticipantID: "6",
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setCardTypedata(response.data.cardTypeList);
      } else {
      }
    } catch (error) {}
  };

  // clear input field
  const handleClick = () => {
    setAccountType("");
    setAccountno("");
    setCardtype("");
    setCardno("");
  };

  // Added the Link card
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      accounttype === "" ||
      accounttype.length === 0 ||
      accountno === "" ||
      cardtype === "" ||
      cardno === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `card-account-linkage/cardAccountLinkage`,
          {
            strParticipantID: "6",
            strAccountType: accounttype,
            strAccountNumber: accountno,
            strCardType: cardtype,
            strCardNumber: cardno,
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
        handleShowError();
      }
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Linkage Card And Account
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1 ">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white ">
                <div className="grid gap-4  md:grid-cols-4 px-4">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Account Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={accounttype}
                      onChange={(e) => setAccountType(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      required
                    >
                      <option value="">Select</option>
                      {type.map((data) => (
                        <option value={data.strAccountType}>
                          {data.strAccountType || ""} -
                          {data.strDescription || ""}
                        </option>
                      ))}
                    </select>
                    {error && accounttype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Account Type
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
                      Account number{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      value={accountno}
                      // onChange={(e) => setAccountno(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setAccountno(value);

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
                        if (accountno.length === 0) {
                          setError("Please Enter Account number!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account number"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && accountno.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Account number!
                      </p>
                    ) : null}
                    {/* {error && accountno.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Account number
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Card Type <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={cardtype}
                      onChange={(e) => setCardtype(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {cardtypedata.map((data) => (
                        // <optgroup key={uuidv4()}>
                        <option value={data.strCardType || "-"}>
                          {data.strCardType}
                          {data.strdescription}
                        </option>
                        // </optgroup>
                      ))}
                    </select>
                    {error && cardtype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Card Type
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
                      Card Number <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      value={cardno}
                      // onChange={(e) => setCardno(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setCardno(value);

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
                        if (cardno.length === 0) {
                          setError("Please Enter Card number!");
                          setErrorMessage1("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Card number"
                    />
                    {errorMessage1 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage1}
                      </p>
                    ) : error && cardno.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Card number!
                      </p>
                    ) : null}
                    {/* {error && cardno.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Card number
                        </p>
                      ) : (
                        ""
                      )} */}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-2 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
      {/* </div> */}

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
