import React from "react";
import AppLayout from "../../layout/AppLayout";
import { useState } from "react";
import amsApi from "../../api/amsApi";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { XMarkIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";

export default function SearchTxn() {
  const [transactionid, setTransactionid] = useState("");
  const [alldata, setAlldata] = useState([]);

  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [checked, setChecked] = useState(true);

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (transactionid === "" || transactionid.length === 0) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `accountTxnMaster/searchTransationByTxnId`,
          {
            strTxn_id: transactionid,
          },
          {
            headers: {
              "Content-type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setAlldata(response.data.accountTranMaster);
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };
  const handleClick = () => {
    setTransactionid("");
    setAlldata([]);
  };

  const Tran_type = alldata.map((t) => [t.strTran_type])[0];
  const Txn_id = alldata.map((t) => [t.strTxn_id])[0];

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-3.5 sm:px-10 rounded-t-lg ">
            <div className=" flex items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Search Transaction View
              </p>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-3.5 focus:outline-none rounded text-xs font-normal">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",
                      "--Switch-trackWidth": "64px",
                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1  bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-3.5 py-1 sm:p-2 bg-white">
                <div className="grid gap-1 md:grid-cols-4 px-8">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Transaction ID
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="transactionid"
                      value={transactionid}
                      // onChange={(e) => setTransactionid(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setTransactionid(value);

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
                        if (transactionid.length === 0) {
                          setError("Please Enter Transaction id!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Transaction id"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && transactionid.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Transaction id!
                      </p>
                    ) : null}
                  </div>
                  <div className="my-4">
                    <button
                      title="Click Search Button"
                      type="submit"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-4 border border-blue-700 rounded  shadow-lg inner"
                    >
                      <MagnifyingGlassIcon className="h-3 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-4 mx-2  font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                    >
                      <XMarkIcon className="h-3 w-3" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>

        {/* Dilog box */}
      </div>
      {/* <div className="bg-white overflow-x-auto relative shadow-md mt-2"> */}
      <div className="table-wrp block max-h-[27rem] w-full">
        <div className="table-wrp block  w-full ">
          <div className="w-auto transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
            <div
              as="h3"
              className="flex text-lg font-medium leading-6 text-black  justify-between bg-blue-300 px-4"
            >
              <p className="px-4 text-sm mt-1">
                <span className="px-1 text-black">Tran Type:</span>
                <span
                  style={{ wordWrap: "break-word" }}
                  className=" border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white "
                >
                  {Tran_type}
                </span>
              </p>
              <p className="px-4 text-sm mt-1">
                <span className="px-1 text-black">Txn ID:</span>
                <span className=" border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white ">
                  {Txn_id}
                </span>
              </p>
            </div>

            <div className="overflow-x-auto  relative shadow-md  w-auto">
              <div className="table-wrp block max-h-[27rem]   w-auto ">
                <table class="shadow-md rounded  w-full m-auto ">
                  <thead class="sticky block top-0" scope="col">
                    <tr class="flex text-left ">
                      <th
                        scope="col"
                        class=" w-full p-1 border bg-white border-r-0 border-gray-300 font-normal"
                      >
                        {/* <h4 class="u-slab">Business</h4> */}
                        <p class=" mt-auto block text-white bg-indigo-500 text-md py-1 text-center rounded font-normal">
                          Debit (From Account)
                        </p>
                      </th>
                      <th
                        scope="col"
                        class=" w-full p-1 border bg-white rounded-tr border-gray-300 font-normal"
                      >
                        <p class=" mt-auto block text-white bg-indigo-500 text-md py-1 text-center rounded font-normal">
                          Credit (To Account)
                        </p>
                      </th>
                    </tr>
                  </thead>

                  {alldata.map(
                    ({
                      strTxn_id,
                      txn_Date,
                      txn_Time,
                      strTran_type,
                      strTransaction_amount,
                      strAccountNumber,
                      strAccountType,
                      strFrom_account_number,
                      strTo_account_number,
                      strAuthCode,
                      strResponseCode,
                    }) => (
                      <tbody>
                        <tr class="flex text-left w-auto">
                          <td class="bg-gray-100 border  flex justify-around sm:justify-around border-t-0  border-gray-300 w-full border-b-0 u-slab text-blue-500">
                            {/* <div class="flex items-center px-2 flex-wrap sm:no-wrap justify-between sm:justify-start"> */}
                            <p class="px-1">
                              <span class="px-1 text-black text-sm ">
                                Date:
                              </span>
                              <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                {txn_Date}
                              </span>
                            </p>
                            <p class="px-1">
                              <span class="px-1 text-black text-sm">Time:</span>
                              <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                {txn_Time}
                              </span>
                            </p>
                            <p class="px-1">
                              <span class="px-1 text-black text-sm">
                                Auth Code:
                              </span>
                              <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                {strAuthCode}
                              </span>
                            </p>
                            <p class="px-1">
                              <span class="px-1 text-black text-sm">
                                Response Code:
                              </span>
                              <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                {strResponseCode}
                              </span>
                            </p>
                            {/* </div> */}
                          </td>
                        </tr>
                        <tr class="flex text-left ">
                          <th
                            scope="col"
                            class=" w-full  border bg-white rounded-tr border-gray-300 font-normal"
                          >
                            <div class="flex mb-2 mt-2 items-left  flex-wrap sm:no-wrap justify-between sm:justify-between">
                              <ul class="flex justify-between   text-sm font-normal sm:block ">
                                <div className="flex    ">
                                  <li class=" w-full px-3 py-1  text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap    ">
                                    Account number
                                  </li>
                                  <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                    {strFrom_account_number}
                                  </p>

                                  {/* <div className="flex p-1 "> */}
                                  <li class=" w-full px-3 py-1 pl-10 text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap  ">
                                    Transaction amount
                                  </li>
                                  <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                    {strTransaction_amount}
                                  </p>
                                </div>
                                {/* </div> */}
                              </ul>
                              {/* <a
                                        href=""
                                        class="mt-auto block px-4 text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                        title=""
                                      >
                                        Total
                                      </a> */}
                            </div>
                          </th>{" "}
                          <th
                            scope="col"
                            class=" w-full  border bg-white rounded-tr border-gray-300 font-normal"
                          >
                            <div class="flex mb-2 mt-2 items-left  flex-wrap sm:no-wrap justify-between sm:justify-between">
                              <ul class="flex justify-between   text-sm font-normal sm:block pl-1 ">
                                <div className="flex p-1  space-x-2   ">
                                  <li class=" w-full px-3 py-1  text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap    ">
                                    Account number
                                  </li>
                                  <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                    {strTo_account_number}
                                  </p>

                                  {/* <div className="flex p-1 "> */}
                                  <li class=" w-full px-3 py-1 pl-20 text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap  ">
                                    Transaction amount
                                  </li>
                                  <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                    {strTransaction_amount}
                                  </p>
                                </div>
                                {/* </div> */}
                              </ul>
                              {/* <a
                                        href=""
                                        class="mt-auto block px-4 text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                        title=""
                                      >
                                        Total
                                      </a> */}
                            </div>
                          </th>
                        </tr>
                      </tbody>
                    )
                  )}

                  {/* <tfoot>
                            <tr class="flex text-left text-sm">
                              <td class="w-1/4 hidden sm:block p-4 border-gray-300 border border-t-0 text-center bg-gray-100 border-r-0"></td>
                              <td class="w-1/3 sm:w-1/4 p-4 border-gray-300 border border-t-0 text-center border-r-0">
                                <a
                                  href=""
                                  class=" mt-auto block text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                  title=""
                                >
                                  Get Started
                                </a>
                              </td>
                              <td class="w-1/3 sm:w-1/4 p-4 border-gray-300 border border-t-0 text-center border-r-0">
                                <a
                                  href=""
                                  class=" mt-auto block text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                  title=""
                                >
                                  Request a demo
                                </a>
                              </td>
                              <td class="w-1/3 sm:w-1/4 p-4 border-gray-300 border border-t-0 text-center">
                                <a
                                  href=""
                                  class=" mt-auto block text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal rounded-bt"
                                  title=""
                                >
                                  Request a demo
                                </a>
                              </td>
                            </tr>
                          </tfoot> */}
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
      {/* </div> */}

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

// <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
//   <div className="flex justify-end items-center sm:px-3.5 lg:px-8 bg-blue-100 ">
//     <div className="flex justify-center rounded-md border border-transparent px-2 mx-2 text-sm font-normal text-white ">
//       {" "}
//       <div className="pt-2 relative mx-auto text-gray-600">
//         <input
//           title="Search Data"
//           type="search"
//           id="search"
//           value={searchKeyword}
//           onChange={handleSearch}
//           className="block w-full px-2 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
//           placeholder="Search.."
//           style={{
//             backgroundColor: checkpoint === 1 ? "white" : "lightgray",
//           }}
//           disabled={checkpoint !== 1}
//         />
//         <button
//           type="submit"
//           className="absolute right-0 top-0 mt-5 mr-4"
//         >
//           <svg
//             xmlns="http://www.w3.org/2000/svg"
//             fill="none"
//             viewBox="0 0 24 24"
//             strokeWidth={1.5}
//             stroke="currentColor"
//             className="w-6 h-6"
//           >
//             <path
//               strokeLinecap="round"
//               strokeLinejoin="round"
//               d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
//             />
//           </svg>
//         </button>
//       </div>
//     </div>
//   </div>

//    <div className="bg-white overflow-x-auto relative shadow-md ">
//     <div className="table-wrp block max-h-[27rem]">
//       <table className="w-full text-xs text-left text-clack dark:text-blue-100">
//         <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
//           <tr>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strTxn_id")}
//             >
//               TXN ID {renderSortArrow("strTxn_id")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("txn_Date")}
//             >
//               TXN DATE {renderSortArrow("txn_Date")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("txn_Time")}
//             >
//               TXN TIME {renderSortArrow("txn_Time")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strTran_type")}
//             >
//               TXN TYPE {renderSortArrow("strTran_type")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strTransaction_amount")}
//             >
//               TXN AMOUNT {renderSortArrow("strTransaction_amount")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strAccountNumber")}
//             >
//               A/c NO {renderSortArrow("strAccountNumber")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strFrom_account_number")}
//             >
//               FROM A/c NO {renderSortArrow("strFrom_account_number")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strTo_account_number")}
//             >
//               TO A/c NO {renderSortArrow("strTo_account_number")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strAuthCode")}
//             >
//               AUTH CODE {renderSortArrow("strAuthCode")}
//             </th>
//             <th
//               scope="col"
//               className="py-2 px-3.5 "
//               onClick={() => handleSort("strResponseCode")}
//             >
//               RESPONSE CODE {renderSortArrow("strResponseCode")}
//             </th>
//           </tr>
//         </thead>
//         <tbody>
//           {sortedData.length > 0 ? (
//             <>
//               {sortedData.map(
//                 ({
//                   strTxn_id,
//                   txn_Date,
//                   txn_Time,
//                   strTran_type,
//                   strTransaction_amount,
//                   strAccountNumber,
//                   strFrom_account_number,
//                   strTo_account_number,
//                   strAuthCode,
//                   strResponseCode,
//                 }) => (
//                   <tr
//                     key={uuidv4()}
//                     className="border-b dark:border-neutral-500"
//                   >
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strTxn_id || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           txn_Date || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           txn_Time || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strTran_type || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap text-right">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strTransaction_amount || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strAccountNumber || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strFrom_account_number || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strTo_account_number || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strAuthCode || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-3.5 py-2 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           strResponseCode || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                   </tr>
//                 )
//               )}
//             </>
//           ) : (
//             <tr>
//               <td colSpan="12" className="px-3.5 py-1 text-center">
//                 <span className="text-sm font-normal text-gray-500">
//                   No Data Available.
//                 </span>
//               </td>
//             </tr>
//           )}
//         </tbody>
//       </table>
//     </div>
//   </div>
