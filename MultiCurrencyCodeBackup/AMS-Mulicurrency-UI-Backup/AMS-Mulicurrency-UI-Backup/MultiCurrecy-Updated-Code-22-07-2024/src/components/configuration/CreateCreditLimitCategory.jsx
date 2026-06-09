import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function CreateCreditLimitCategory() {
  const [creditlimit, setCreditLimit] = useState("");
  const [creditcategory, setCreditCategory] = useState("");
  const [alldata, setAlldata] = useState([]);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [cashWdlCreditLimit, setcashWdlCreditLimit] = useState("");
  // const [purchaseTxnCount, setpurchaseTxnCount] = useState("");
  // const [cashWdlTxnCount, setcashWdlTxnCount] = useState("");
  const [purchasePerTxnLimit, setpurchasePerTxnLimit] = useState("");
  const [cashWdlPerTxnLimit, setcashWdlPerTxnLimit] = useState("");
  const [purchaseTxnPerDayCount, setpurchaseTxnPerDayCount] = useState("");
  const [purchaseTxnPerMonthCount, setpurchaseTxnPerMonthCount] = useState("");
  const [cashWdlTxnPerDayCount, setcashWdlTxnPerDayCount] = useState("");
  const [cashWdlTxnPerMonthCount, setcashWdlTxnPerMonthCount] = useState("");
  const [purchaseCreditLimit, setpurchaseCreditLimit] = useState("");

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

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      purchaseCreditLimit === "" ||
      purchaseCreditLimit.length === 0 ||
      creditcategory === "" ||
      creditcategory.length === 0
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `credit_limit/isCreditTypeExst`,
          {
            strCreditType: creditcategory,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code !== "S0000") {
          const response1 = await amsApi.post(`credit_limit/addcreditAccount`, {
            strParticipantId: participantID,
            strCreditType: creditcategory,
            purchaseCreditLimit: purchaseCreditLimit,
            purchasePerTxnLimit: purchasePerTxnLimit,
            cashWdlCreditLimit: cashWdlCreditLimit,
            cashWdlPerTxnLimit: cashWdlPerTxnLimit,
            // purchaseTxnCount: purchaseTxnCount,
            purchaseTxnPerDayCount: purchaseTxnPerDayCount,
            purchaseTxnPerMonthCount: purchaseTxnPerMonthCount,
            // cashWdlTxnCount: cashWdlTxnCount,
            cashWdlTxnPerDayCount: cashWdlTxnPerDayCount,
            cashWdlTxnPerMonthCount: cashWdlTxnPerMonthCount,
          });
          if (response1.data.code === "S0000") {
            handleShowSuccess(response1.data.message);
            setError("");

            handleClick();
          } else {
            handleShowError(response1.data.message);
          }
        } else {
          handleShowError("Duplicate Account Type. Account Already Exist!");
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  // clear input data
  const handleClick = () => {
    setCreditLimit("");
    setCreditCategory("");
    setpurchaseCreditLimit("");
    setpurchasePerTxnLimit("");
    setcashWdlCreditLimit("");
    setcashWdlPerTxnLimit("");
    // setpurchaseTxnCount("");
    setpurchaseTxnPerDayCount("");
    setpurchaseTxnPerMonthCount("");
    // setcashWdlTxnCount("");
    setcashWdlTxnPerDayCount("");
    setcashWdlTxnPerMonthCount("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Create Credit Limit Category Configuration
              </p>
            </div>
          </div>
        </div>
        <div className="bg-white sm:rounded-md">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-1 sm:p-2 bg-white  ">
                <div className="flex mb-2 ">
                  <label
                    htmlFor="text px-8 "
                    className="block w-auto px-3 py-1  text-xs font-normal text-gray-700 bg-white  mr-20 ml-1 "
                  >
                    Credit Category{""}
                    <span className="text-red-600 px-1  ">*</span>
                  </label>
                  <input
                    type="text"
                    id="DefineCreditCategory"
                    value={creditcategory}
                    onChange={(e) => setCreditCategory(e.target.value)}
                    className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Credit Category"
                    autoComplete="off"
                  />
                  {error && creditcategory.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please enter Credit Category
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div className="grid gap-2  grid-cols-2  ">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-12 py-1 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Purchase Credit Limit
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="purchaseCreditLimit"
                      value={purchaseCreditLimit}
                      onChange={(e) => {
                        const value = e.target.value;
                        setpurchaseCreditLimit(value);

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
                        if (purchaseCreditLimit.length === 0) {
                          setError("Please enter Credit Limit!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Purchase Credit Limit"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && creditlimit.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Purchase Credit Limit!
                      </p>
                    ) : null}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 text-xs font-normal text-gray-700 bg-white mr-12 "
                    >
                      Purchase Per Txn Limit{""}
                      <span className="text-red-600 px-3 ml-1 ">*</span>
                    </label>
                    <input
                      type="text"
                      id="purchasePerTxnLimit"
                      value={purchasePerTxnLimit}
                      onChange={(e) => setpurchasePerTxnLimit(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Purchase Per Txn Limit"
                      autoComplete="off"
                    />
                    {error && purchasePerTxnLimit.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Purchase Per Txn Limit
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-4  text-xs font-normal text-gray-700 bg-white  "
                    >
                      Cash Withdrawal Credit Limit{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="cashWdlCreditLimit"
                      value={cashWdlCreditLimit}
                      onChange={(e) => setcashWdlCreditLimit(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Cash Withdrawal Credit Limit"
                      autoComplete="off"
                    />
                    {error && cashWdlCreditLimit.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Cash Withdrawal Credit Limit
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-7  text-xs font-normal text-gray-700 bg-white  "
                    >
                      Cash Withdrawal Per Txn Limit{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="cashWdlPerTxnLimit"
                      value={cashWdlPerTxnLimit}
                      onChange={(e) => setcashWdlPerTxnLimit(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Cash Withdrawal Per Txn Limit"
                      autoComplete="off"
                    />
                    {error && cashWdlPerTxnLimit.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Cash Withdrawal Per Txn Limit
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {/* <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-16 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Purchase Txn Count{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="purchaseTxnCount"
                      value={purchaseTxnCount}
                      onChange={(e) => setpurchaseTxnCount(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Purchase Txn Count"
                      autoComplete="off"
                    />
                    {error && purchaseTxnCount.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Purchase Txn Count
                      </p>
                    ) : (
                      ""
                    )}
                  </div> */}
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-6 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Purchase Txn Per Day Count{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="purchaseTxnPerDayCount"
                      value={purchaseTxnPerDayCount}
                      onChange={(e) =>
                        setpurchaseTxnPerDayCount(e.target.value)
                      }
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Purchase Txn Per Day Count"
                      autoComplete="off"
                    />
                    {error && purchaseTxnPerDayCount.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Purchase Txn Per Day Count
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-2 py-1 mr-8 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Purchase Txn Per Month Count{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="purchaseTxnPerMonthCount"
                      value={purchaseTxnPerMonthCount}
                      onChange={(e) =>
                        setpurchaseTxnPerMonthCount(e.target.value)
                      }
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Purchase Txn Per Month Count"
                      autoComplete="off"
                    />
                    {error && purchaseTxnPerMonthCount.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Purchase Txn Per Month Count
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  {/* <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-1 py-1 mr-14 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Cash Withdrawal Txn Count{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="cashWdlTxnCount"
                      value={cashWdlTxnCount}
                      onChange={(e) => setcashWdlTxnCount(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Cash Withdrawal Txn Count"
                      autoComplete="off"
                    />
                    {error && cashWdlTxnCount.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Cash Withdrawal Txn Count
                      </p>
                    ) : (
                      ""
                    )}
                  </div> */}

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-1 py-1 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Cash Withdrawal Txn Per Day Count{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="cashWdlTxnPerDayCount"
                      value={cashWdlTxnPerDayCount}
                      onChange={(e) => setcashWdlTxnPerDayCount(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Cash Withdrawal Txn Per Day Count"
                      autoComplete="off"
                    />
                    {error && cashWdlTxnPerDayCount.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Cash Withdrawal Txn Per day Count
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-1 py-1 text-xs font-normal text-gray-700 bg-white  "
                    >
                      Cash Withdrawal Txn Per Month Count{""}
                      <span className="text-red-600 px-1  ">*</span>
                    </label>
                    <input
                      type="text"
                      id="cashWdlTxnPerMonthCount"
                      value={cashWdlTxnPerMonthCount}
                      onChange={(e) =>
                        setcashWdlTxnPerMonthCount(e.target.value)
                      }
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Cash Withdrawal Txn Per Month Count"
                      autoComplete="off"
                    />
                    {error && cashWdlTxnPerMonthCount.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please enter Cash Withdrawal Txn Per Month Count
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-4 mx-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-4 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
        {/* <div className="flex justify-end items-center sm:px-2 lg:px-2 bg-blue-100   rounded-t-lg ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-xs  font-medium text-white ">
            <div className=" relative  mx-auto text-gray-600">
              <input
                title="Search Data"
                type="text"
                value={searchKeyword}
                onChange={handleSearch}
                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  // You can set other styles here as needed
                }}
                disabled={checkpoint !== 1}
              />
              {""}
              <button
                type="submit"
                className="absolute right-0 top-0 mt-2 mr-1   "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div> */}

        {/* <div className="overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[12 rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-1.5 px-6">
                    credit type
                  </th>
                  <th scope="col" className="py-1.5 px-6">
                    credit limit
                  </th>
                  <th scope="col" className="py-1.5 px-6">
                    edit
                  </th>
                </tr>
              </thead>

              <tbody>
                {filteredData.length > 0 ? (
                  <>
                    {filteredData.map(
                      ({ strCreditType, strCreditLimit, strID }) => (
                        <tr
                          key={strID}
                          className="border-b dark:border-neutral-500"
                        >
                          <td className="px-6 py-1.5 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={strCreditType}
                                onChange={(e) =>
                                  handleEditChange(e, strID, "strCreditType")
                                }
                                className=" border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xsclassName text-gray-900">
                                {highlightKeyword(
                                  strCreditType || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="px-6 py-1.5 whitespace-nowrap">
                            {editingRow === strID ? (
                              <input
                                type="text"
                                value={strCreditLimit}
                                onChange={(e) =>
                                  handleEditChange(e, strID, "strCreditLimit")
                                }
                                className="border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                              />
                            ) : (
                              <div className="text-xsclassName text-gray-900">
                                {highlightKeyword(
                                  strCreditLimit || "",
                                  searchKeyword
                                )}
                              </div>
                            )}
                          </td>
                          <td className="whitespace-nowrap">
                            {editingRow === strID ? (
                              <div>
                                <button
                                  title="Click Update Button"
                                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                                  onClick={() => handleUpdate(strID)}
                                >
                                  Update
                                </button>
                                <button
                                  title="Click Cancel Button"
                                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-2  mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                                  onClick={() => cancelEdit()}
                                >
                                  Cancel
                                </button>
                              </div>
                            ) : (
                              <button
                                title="Click Edit Button"
                                // onClick={() => proCategoryViewModel(id)}
                                type="button"
                                onClick={() => startEdit(strID)}
                                className="px-6 py-2.5 whitespace-nowrap"
                              >
                                <svg
                                  className="w-5 h-5 ml-1"
                                  fill="currentColor"
                                  viewBox="0 0 20 20"
                                  xmlns="http://www.w3.org/2000/svg"
                                >
                                  <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"></path>
                                  <path
                                    fillRule="evenodd"
                                    d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
                                    clipRule="evenodd"
                                  ></path>
                                </svg>
                              </button>
                            )}
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <tr>
                    <td colSpan="12" className="px-6 py-2.5 text-center">
                      <span className="text-lg font-medium text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div> */}
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
